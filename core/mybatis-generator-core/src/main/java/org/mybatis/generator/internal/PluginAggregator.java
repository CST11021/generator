/*
 *    Copyright 2006-2023 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.internal;

import org.mybatis.generator.api.CompositePlugin;
import org.mybatis.generator.config.Context;

import java.util.List;
import java.util.Properties;

/**
 * 此类仅供内部使用。它包含当前上下文的插件列表，用于将插件聚合在一起。此类实施规则，如果任何插件从方法返回“false”，则不会调用后续插件。 <p>此类不遵循正常的插件生命周期，不应由客户端子类化。
 *
 * @author Jeff Butler
 *
 */
public final class PluginAggregator extends CompositePlugin {

    @Override
    public void setContext(Context context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setProperties(Properties properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean validate(List<String> warnings) {
        throw new UnsupportedOperationException();
    }
}
