# -*- coding: utf-8 -*-
"""
配置加载和YAML数据加载工具
统一管理测试配置和测试数据
"""
import yaml
from pathlib import Path
from typing import Any, Dict, List


class ConfigLoader:
    """配置加载器 - 单例模式"""

    _instance = None
    _config = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance

    def __init__(self):
        if self._config is None:
            self._load_config()

    def _load_config(self):
        """从YAML文件加载配置"""
        config_path = Path(__file__).parent.parent / 'config' / 'config.yaml'
        with open(config_path, 'r', encoding='utf-8') as f:
            self._config = yaml.safe_load(f)

    @property
    def base_url(self):
        """获取基础URL"""
        return self._config['env']['base_url']

    @property
    def timeout(self):
        """获取超时时间"""
        return self._config['env'].get('timeout', 10)

    def get(self, key, default=None):
        """获取配置项"""
        return self._config.get(key, default)


class YamlDataLoader:
    """YAML数据加载器 - 封装YAML读取逻辑"""

    @staticmethod
    def load(file_path: str) -> Dict[str, Any]:
        """
        加载单个YAML文件

        Args:
            file_path: YAML文件路径（相对于data目录）

        Returns:
            字典格式的测试数据
        """
        full_path = Path(__file__).parent.parent / 'data' / file_path
        with open(full_path, 'r', encoding='utf-8') as f:
            return yaml.safe_load(f)

    @staticmethod
    def load_test_cases(file_path: str) -> List[Dict[str, Any]]:
        """
        加载测试用例数据（支持用例列表格式）

        Args:
            file_path: YAML文件路径

        Returns:
            测试用例列表
        """
        data = YamlDataLoader.load(file_path)
        if isinstance(data, dict) and 'test_cases' in data:
            return data['test_cases']
        return data if isinstance(data, list) else [data]


# 全局配置实例 - 导出给其他模块使用
config = ConfigLoader()

# 全局数据加载器实例
yaml_loader = YamlDataLoader()
