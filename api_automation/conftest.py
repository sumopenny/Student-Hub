# -*- coding: utf-8 -*-
"""
pytest配置文件
定义测试夹具和全局配置
"""
import pytest
from core.http_client import http_client
from core.yaml_loader import yaml_loader


@pytest.fixture(scope='session')
def api_client():
    """
    会话级别的API客户端
    整个测试会话只创建一次
    """
    yield http_client
    http_client.close()


@pytest.fixture(scope='session')
def test_data():
    """
    加载测试数据
    返回包含所有测试数据的字典
    """
    return {
        'login': yaml_loader.load('login_data.yaml'),
        'student': yaml_loader.load('student_data.yaml')
    }


@pytest.fixture
def login_data(test_data):
    """登录测试数据"""
    return test_data['login']


@pytest.fixture
def student_data(test_data):
    """学生管理测试数据"""
    return test_data['student']
