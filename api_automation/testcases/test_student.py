# -*- coding: utf-8 -*-
"""
学生管理模块测试用例
采用数据驱动模式，测试数据从YAML文件读取
"""
import pytest
import allure
from api.student_api import StudentAPI
from api.login_api import LoginAPI
from core.http_client import http_client
from core.assertions import assert_response


@pytest.fixture(scope='class')
def login_and_get_token():
    """先登录获取Token，并设置到http_client中"""
    # 先登录获取token
    response = LoginAPI.login('admin', 'hhxx985211')
    result = response.json()
    
    if result.get('code') == 1 and result.get('data') and result['data'].get('token'):
        token = result['data']['token']
        # 设置到http_client的请求头中
        http_client.set_token(token)
        return token
    return None


@allure.feature("学生管理模块")
class TestStudentQuery:
    """学生查询接口测试类"""

    @allure.story("查询学生")
    @allure.title("获取学生详情-成功")
    def test_get_student_success(self, student_data, login_and_get_token):
        """测试获取学生详情成功"""
        data = student_data['get_student_success']

        with allure.step("发送获取学生请求"):
            response = StudentAPI.get_student(data['id'])

        with allure.step("验证响应"):
            print(f"\n实际响应状态码: {response.status_code}")
            print(f"实际响应内容: {response.text}")
            assert response.status_code in [200, 201], f"状态码应为200或201，实际: {response.status_code}"

    @allure.story("查询学生")
    @allure.title("获取学生详情-学生不存在")
    def test_get_student_not_exists(self, student_data, login_and_get_token):
        """测试获取不存在的学生"""
        data = student_data['get_student_not_exists']

        with allure.step("发送获取学生请求"):
            response = StudentAPI.get_student(data['id'])

        with allure.step("验证响应"):
            print(f"\n实际响应状态码: {response.status_code}")
            print(f"实际响应内容: {response.text}")
            assert response.status_code in [200, 404, 400], f"状态码异常: {response.status_code}"

    @allure.story("查询学生")
    @allure.title("分页查询学生列表")
    def test_list_students_default(self, student_data, login_and_get_token):
        """测试分页查询学生列表"""
        data = student_data['list_students_default']

        with allure.step("发送查询请求"):
            response = StudentAPI.list_students(
                page=data['page'],
                page_size=data['page_size']
            )

        with allure.step("验证响应"):
            print(f"\n实际响应状态码: {response.status_code}")
            print(f"实际响应内容: {response.text}")
            assert response.status_code in [200, 201], f"状态码应为200或201，实际: {response.status_code}"

    @allure.story("查询学生")
    @allure.title("带条件查询学生列表")
    def test_list_students_with_filter(self, student_data, login_and_get_token):
        """测试带条件查询学生列表"""
        data = student_data['list_students_with_filter']

        with allure.step("发送查询请求"):
            response = StudentAPI.list_students(
                page=data['page'],
                page_size=data['page_size'],
                name=data.get('name'),
                gender=data.get('gender')
            )

        with allure.step("验证响应"):
            print(f"\n实际响应状态码: {response.status_code}")
            print(f"实际响应内容: {response.text}")
            assert response.status_code in [200, 201], f"状态码应为200或201，实际: {response.status_code}"
