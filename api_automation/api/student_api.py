# -*- coding: utf-8 -*-
"""
API接口层 - 学生管理模块
封装学生管理相关的API调用
"""
from core.http_client import http_client
import requests
from typing import Optional, Dict, Any


class StudentAPI:
    """学生管理接口封装"""

    @staticmethod
    def add_student(
        name: str,
        gender: str,
        username: str,
        password: str,
        phone: Optional[str] = None,
        email: Optional[str] = None,
        address: Optional[str] = None,
        age: Optional[int] = None
    ) -> requests.Response:
        """
        添加学生

        Args:
            name: 姓名
            gender: 性别
            username: 用户名
            password: 密码
            phone: 电话
            email: 邮箱
            address: 地址
            age: 年龄

        Returns:
            HTTP响应对象
        """
        data = {
            'name': name,
            'gender': gender,
            'username': username,
            'password': password
        }
        if phone:
            data['phone'] = phone
        if email:
            data['email'] = email
        if address:
            data['address'] = address
        if age is not None:
            data['age'] = age

        return http_client.post('/stus', json=data)

    @staticmethod
    def get_student(stu_id: int) -> requests.Response:
        """
        获取学生详情

        Args:
            stu_id: 学生ID

        Returns:
            HTTP响应对象
        """
        return http_client.get(f'/stus/{stu_id}')

    @staticmethod
    def update_student(student_data: Dict[str, Any]) -> requests.Response:
        """
        更新学生信息

        Args:
            student_data: 学生信息字典

        Returns:
            HTTP响应对象
        """
        return http_client.put('/stus', json=student_data)

    @staticmethod
    def delete_student(stu_id: int) -> requests.Response:
        """
        删除学生

        Args:
            stu_id: 学生ID

        Returns:
            HTTP响应对象
        """
        return http_client.delete('/stus', params={'id': stu_id})

    @staticmethod
    def list_students(
        page: int = 1,
        page_size: int = 10,
        name: Optional[str] = None,
        address: Optional[str] = None,
        gender: Optional[str] = None,
        age: Optional[int] = None
    ) -> requests.Response:
        """
        分页查询学生列表

        Args:
            page: 页码
            page_size: 每页数量
            name: 姓名（模糊查询）
            address: 地址
            gender: 性别
            age: 年龄

        Returns:
            HTTP响应对象
        """
        params = {'page': page, 'pageSize': page_size}
        if name:
            params['name'] = name
        if address:
            params['address'] = address
        if gender:
            params['gender'] = gender
        if age is not None:
            params['age'] = age

        return http_client.get('/stus', params=params)

    @staticmethod
    def update_password(stu_id: int, old_password: str, new_password: str) -> requests.Response:
        """
        修改密码

        Args:
            stu_id: 学生ID
            old_password: 旧密码
            new_password: 新密码

        Returns:
            HTTP响应对象
        """
        return http_client.post(
            '/stus/updatePassword',
            json={
                'id': str(stu_id),
                'oldPassword': old_password,
                'newPassword': new_password
            }
        )
