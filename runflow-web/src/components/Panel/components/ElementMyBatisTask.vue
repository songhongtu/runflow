<template>
  <n-collapse-item name="element-start-initiator">
    <template #header>
      <collapse-title title="mybatis任务">
        <lucide-icon name="PlayCircle"/>
      </collapse-title>
    </template>
    <div class="element-start-initiator">

      <edit-item label="是否异步">
        <n-switch placeholder="" v-model:value="isAsync"
                  @change="(v)=>setStringValueByKey(getActive,'isAsync',v)"/>
      </edit-item>




      <edit-item label="statementId">
        <n-input placeholder="" v-model:value="statementId"
                 @change="(v)=>setStringValueByKey(getActive,'statementId',v)"/>
      </edit-item>
      <edit-item label="查询类型">
        <n-select placeholder="" v-model:value="selectType" :options="selectTypeOption"
                  @change="(v)=>setStringValueByKey(getActive,'selectType',v)"/>
      </edit-item>
      <edit-item label="是否分页">
        <n-switch v-model:value="isPage"
                    @change="(v)=>setStringValueByKey(getActive,'isPage',v)"/>
      </edit-item>
      <edit-item v-if="isPage" label="当前页">
        <n-input placeholder="" v-model:value="pageNum"
                 @change="(v)=>setStringValueByKey(getActive,'pageNum',v)"/>
      </edit-item>
      <edit-item v-if="isPage" label="分页大小">
        <n-input placeholder="" v-model:value="pageSize"
                 @change="(v)=>setStringValueByKey(getActive,'pageSize',v)"/>
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
  name: 'ElementMyBatisTask',
  setup() {
    const modelerStore = modeler()
    const getActive = computed<Base | undefined>(() => modelerStore.getActive!)
    const selectType = ref<string | undefined>('SELECTLIST')
    const statementId = ref<string | undefined>('')
    const selectTypeOption = ref<Record<string, string>[]>([
      {label: 'selectOne', value: 'SELECTONE'},
      {label: 'selectList', value: 'SELECTLIST'},
      {label: 'insert', value: 'INSERT'},
      {label: 'delete', value: 'DELETE'},
    ])
    const pageNum = ref<number | undefined>(0)
    const pageSize = ref<number | undefined>(10)
    const isPage = ref<boolean>(false)

    const isAsync = ref<boolean | false>(true)
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
      getElementStringValueByKey(statementId, 'statementId');
      getElementStringValueByKey(selectType, 'selectType');
      getElementStringValueByKeyBoolean(isPage, 'isPage');
      getElementStringValueByKey(pageNum, 'pageNum');
      getElementStringValueByKey(pageSize, 'pageSize');
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
      statementId, selectType, selectTypeOption, isPage, pageNum, pageSize,
      setStringValueByKey,isAsync
    }
  }
})
</script>
