# -*- coding: utf-8 -*-
"""
HTTP请求封装层
统一处理接口调用
"""
import requests
from typing import Any, Dict, Optional
from core.yaml_loader import config


class HttpClient:
    """
    HTTP客户端封装
    统一处理请求发送、响应返回、会话管理
    """

    def __init__(self):
        """初始化会话和基础配置"""
        self.session = requests.Session()
        self.base_url = config.base_url
        self.timeout = config.timeout
        self.token = None  # 存储登录Token

    def _get_headers(self, headers: Optional[Dict] = None) -> Dict:
        """
        构建请求头

        Args:
            headers: 额外的请求头

        Returns:
            合并后的请求头字典
        """
        default_headers = {
            'Content-Type': 'application/json'
        }
        if self.token:
            # Token格式：Bearer + 空格 + token
            default_headers['Authorization'] = f'Bearer {self.token}'

        if headers:
            default_headers.update(headers)
        return default_headers

    def set_token(self, token: str):
        """设置认证Token"""
        self.token = token

    def request(
        self,
        method: str,
        url: str,
        params: Optional[Dict] = None,
        json: Optional[Dict] = None,
        data: Optional[Any] = None,
        headers: Optional[Dict] = None,
        **kwargs
    ) -> requests.Response:
        """
        发送HTTP请求

        Args:
            method: HTTP方法（GET/POST/PUT/DELETE）
            url: 请求URL
            params: URL查询参数
            json: JSON请求体
            data: 表单数据
            headers: 自定义请求头

        Returns:
            响应对象
        """
        full_url = f"{self.base_url}{url}" if not url.startswith('http') else url

        response = self.session.request(
            method=method.upper(),
            url=full_url,
            params=params,
            json=json,
            data=data,
            headers=self._get_headers(headers),
            timeout=self.timeout,
            **kwargs
        )
        return response

    def get(self, url: str, **kwargs) -> requests.Response:
        """GET请求"""
        return self.request('GET', url, **kwargs)

    def post(self, url: str, **kwargs) -> requests.Response:
        """POST请求"""
        return self.request('POST', url, **kwargs)

    def put(self, url: str, **kwargs) -> requests.Response:
        """PUT请求"""
        return self.request('PUT', url, **kwargs)

    def delete(self, url: str, **kwargs) -> requests.Response:
        """DELETE请求"""
        return self.request('DELETE', url, **kwargs)

    def close(self):
        """关闭会话"""
        self.session.close()


# 全局HTTP客户端实例
http_client = HttpClient()
