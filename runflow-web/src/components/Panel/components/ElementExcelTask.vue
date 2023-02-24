<template>
  <n-collapse-item name="element-start-initiator">
    <template #header>
      <collapse-title title="excel任务">
        <lucide-icon name="PlayCircle"/>
      </collapse-title>
    </template>
    <div class="element-start-initiator">

      <edit-item label="是否异步">
        <n-switch placeholder="" v-model:value="isAsync"
                  @change="(v)=>setStringValueByKey(getActive,'isAsync',v)"/>
      </edit-item>

      <edit-item label="表达式">
        <n-input placeholder="list集合的el表达式" v-model:value="excelExpression"
                 @change="(v)=>setStringValueByKey(getActive,'excelExpression',v)"/>
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
  name: 'ElementExcelTask',
  setup() {
    const modelerStore = modeler()
    const getActive = computed<Base | null>(() => modelerStore.getActive!)
    const isAsync = ref<boolean | false>(false)
    const excelExpression = ref<string | undefined>('')

    const getElementStringValueByKey = (f, key: string) => {
      let stringValueByKey = getStringValueByKey(getActive.value!, key);
      f.value = stringValueByKey
      return stringValueByKey;
    }

    const getElementStringValueByKeyBoolean = (f, key: string) => {
      let stringValueByKey = getStringValueByKeyBoolean(getActive.value!, key);
      f.value = Boolean(stringValueByKey)
      return stringValueByKey;
    }

    const refresh = () => {
      getElementStringValueByKey(excelExpression, 'excelExpression');
      getElementStringValueByKeyBoolean(isAsync, 'isAsync');


    }
    onMounted(() => {
      refresh()
      EventEmitter.on('element-update', () => {
        refresh()
      })
    })
    return {
      getActive,
      excelExpression,
      setStringValueByKey,isAsync
    }
  }
})
</script>
