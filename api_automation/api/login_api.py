# -*- coding: utf-8 -*-
"""
API接口层 - 登录模块
封装登录相关的API调用
"""
from core.http_client import http_client
from core.assertions import Assertions
import requests


class LoginAPI:
    """登录接口封装"""

    @staticmethod
    def login(username: str, password: str) -> requests.Response:
        """
        用户登录

        Args:
            username: 用户名
            password: 密码

        Returns:
            HTTP响应对象
        """
        return http_client.post(
            '/login',
            json={'username': username, 'password': password}
        )

    @staticmethod
    def register(username: str, password: str, name: str, gender: str) -> requests.Response:
        """
        用户注册

        Args:
            username: 用户名
            password: 密码
            name: 姓名
            gender: 性别

        Returns:
            HTTP响应对象
        """
        return http_client.post(
            '/register',
            json={
                'username': username,
                'password': password,
                'name': name,
                'gender': gender
            }
        )

    @staticmethod
    def check_username(username: str) -> requests.Response:
        """
        检查用户名是否存在

        Args:
            username: 用户名

        Returns:
            HTTP响应对象
        """
        return http_client.get(
            '/checkUsername',
            params={'username': username}
        )
