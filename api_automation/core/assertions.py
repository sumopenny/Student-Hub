# -*- coding: utf-8 -*-
"""
断言机制模块
封装统一的断言逻辑，支持JSON响应断言
"""
import requests
from typing import Any, Dict, Optional, Union


class Assertions:
    """
    断言工具类
    提供常用的断言方法，支持链式调用
    """

    def __init__(self, response: requests.Response):
        """
        初始化断言器

        Args:
            response: HTTP响应对象
        """
        self.response = response
        self._result = None  # 缓存解析后的响应体

    @property
    def json(self) -> Dict[str, Any]:
        """获取JSON响应体"""
        if self._result is None:
            try:
                self._result = self.response.json()
            except ValueError:
                self._result = {}
        return self._result

    @property
    def status_code(self) -> int:
        """获取HTTP状态码"""
        return self.response.status_code

    def equals(self, actual: Any, expected: Any, msg: str = "") -> 'Assertions':
        """
        断言相等

        Args:
            actual: 实际值
            expected: 期望值
            msg: 断言失败时的提示信息

        Returns:
            self，支持链式调用
        """
        assert actual == expected, f"{msg} 期望: {expected}, 实际: {actual}"
        return self

    def not_equals(self, actual: Any, expected: Any, msg: str = "") -> 'Assertions':
        """断言不相等"""
        assert actual != expected, f"{msg} 不应等于: {expected}"
        return self

    def status_code_equals(self, expected: int, msg: str = "") -> 'Assertions':
        """断言状态码"""
        return self.equals(self.status_code, expected, msg)

    def success(self, msg: str = "") -> 'Assertions':
        """
        断言业务成功（根据项目统一响应格式判断）
        项目返回格式为: {"code": 1, "msg": "...", "data": ...}
        code=1 表示成功

        Args:
            msg: 错误提示

        Returns:
            self
        """
        assert self.status_code == 200, f"{msg} HTTP状态码应为200"
        # 如果返回JSON，进一步检查code字段（1表示成功）
        if self.json:
            assert self.json.get('code') == 1, f"{msg} 业务code应为1，实际: {self.json.get('code')}"
        return self

    def error(self, msg: str = "") -> 'Assertions':
        """断言业务失败（code != 1）"""
        if self.json:
            assert self.json.get('code') != 1, f"{msg} 业务应返回错误"
        return self

    def contains(self, container: Union[str, list, dict], value: Any, msg: str = "") -> 'Assertions':
        """断言包含"""
        assert value in container, f"{msg} {container} 应包含 {value}"
        return self

    def not_contains(self, container: Union[str, list, dict], value: Any, msg: str = "") -> 'Assertions':
        """断言不包含"""
        assert value not in container, f"{msg} {container} 不应包含 {value}"
        return self

    def is_true(self, actual: Any, msg: str = "") -> 'Assertions':
        """断言为True"""
        assert actual is True, f"{msg} 应为True"
        return self

    def is_false(self, actual: Any, msg: str = "") -> 'Assertions':
        """断言为False"""
        assert actual is False, f"{msg} 应为False"
        return self

    def is_not_none(self, actual: Any, msg: str = "") -> 'Assertions':
        """断言不为None"""
        assert actual is not None, f"{msg} 不应为None"
        return self

    def is_none(self, actual: Any, msg: str = "") -> 'Assertions':
        """断言为None"""
        assert actual is None, f"{msg} 应为None"
        return self

    def length_equals(self, actual: Union[str, list, dict], expected: int, msg: str = "") -> 'Assertions':
        """断言长度"""
        assert len(actual) == expected, f"{msg} 长度应为{expected}，实际: {len(actual)}"
        return self

    def greater_than(self, actual: Union[int, float], expected: Union[int, float], msg: str = "") -> 'Assertions':
        """断言大于"""
        assert actual > expected, f"{msg} {actual} 应大于 {expected}"
        return self

    def less_than(self, actual: Union[int, float], expected: Union[int, float], msg: str = "") -> 'Assertions':
        """断言小于"""
        assert actual < expected, f"{msg} {actual} 应小于 {expected}"
        return self


# 断言辅助函数 - 简化调用
def assert_response(response: requests.Response) -> Assertions:
    """
    创建断言对象的快捷函数

    Args:
        response: HTTP响应对象

    Returns:
        Assertions断言对象
    """
    return Assertions(response)
