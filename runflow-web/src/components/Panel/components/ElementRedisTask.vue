<template>
  <n-collapse-item name="element-start-initiator">
    <template #header>
      <collapse-title title="redis任务">
        <lucide-icon name="PlayCircle"/>
      </collapse-title>
    </template>
    <div class="element-start-initiator">
      <edit-item label="类型">
        <n-select placeholder="" v-model:value="type" :options="typeOption"
                  @change="(v)=>setStringValueByKey(getActive,'type',v)"/>
      </edit-item>

      <edit-item label="表达式">
        <n-input placeholder="" v-model:value="redisExpression"
                 @change="(v)=>setStringValueByKey(getActive,'redisExpression',v)"/>
      </edit-item>


    </div>
  </n-collapse-item>
</template>

<script lang="ts">
import {computed, defineComponent, onMounted, ref} from 'vue'
import {
  getSkipExpression,
  setSkipExpression,
  getAsync,
  setAsync,
  setCalledElement,
  getCalledElement,
  getStringValueByKey, setStringValueByKey, getStringValueByKeyBoolean
} from '@/bo-utils/initiatorUtil'
import modeler from '@/store/modeler'
import {Base} from 'diagram-js/lib/model'
import EventEmitter from '@/utils/EventEmitter'

export default defineComponent({
  name: 'ElementRedisTask',
  setup() {
    const modelerStore = modeler()
    const getActive = computed<Base | null>(() => modelerStore.getActive!)
    const type = ref<string | undefined>('STRING')


    const typeOption = ref<Record<string, string>[]>([
      {label: 'string', value: 'STRING'},
      {label: 'list', value: 'LIST'},
      {label: 'set', value: 'SET'},
      {label: 'zset', value: 'ZSET'},
      {label: 'hash', value: 'HASH'},
    ])
    const redisExpression = ref<string | undefined>('')

    const getElementStringValueByKey = (f, key: string) => {
      let stringValueByKey = getStringValueByKey(getActive.value!, key);
      f.value = stringValueByKey
      return stringValueByKey;
    }



    const refresh = () => {
      getElementStringValueByKey(redisExpression, 'redisExpression');
      getElementStringValueByKey(type, 'type');
    }
    onMounted(() => {
      refresh()
      EventEmitter.on('element-update', () => {
        refresh()
      })
    })
    return {
      getActive,
      type, typeOption, redisExpression,
      setStringValueByKey
    }
  }
})
</script>
