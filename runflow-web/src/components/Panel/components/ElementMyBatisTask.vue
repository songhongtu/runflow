<template>
  <n-collapse-item name="element-start-initiator">
    <template #header>
      <collapse-title title="mybatis任务">
        <lucide-icon name="PlayCircle"/>
      </collapse-title>
    </template>
    <div class="element-start-initiator">


      <edit-item label="statementId">
        <n-input placeholder="${...}" v-model:value="statementId"
                 @change="(v)=>setStringValueByKey(getActive,'statementId',v)"/>
      </edit-item>


      <edit-item label="selectType">
        <n-input placeholder="${...}" v-model:value="selectType"

                  @change="(v)=>setStringValueByKey(getActive,'selectType',v)"/>
      </edit-item>

      <edit-item v-if="getActive?.type!='bpmn:CallActivity'" label="表达式">
        <n-input placeholder="${...}" v-model:value="initiator" @change="setElementInitiator"/>
      </edit-item>

      <edit-item v-if="getActive?.type=='bpmn:CallActivity'" label="流程key">
        <n-input placeholder="子流程key" v-model:value="calledElement" @change="setElementCalledElement"/>
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
  getStringValueByKey, setStringValueByKey
} from '@/bo-utils/initiatorUtil'
import modeler from '@/store/modeler'
import {Base} from 'diagram-js/lib/model'
import EventEmitter from '@/utils/EventEmitter'

export default defineComponent({
  name: 'ElementMyBatisTask',
  setup() {
    const modelerStore = modeler()
    const getActive = computed<Base | null>(() => modelerStore.getActive!)


    const selectType = ref<string | undefined>('')

    const statementId = ref<string | undefined>('')

    const pageNum = ref<number | undefined>(0)

    const pageSize = ref<number | undefined>(10)

    const isPage = ref<boolean | undefined>(false)

    const initiator = ref<string | undefined>('')
    const isAsync = ref<boolean | false>(false)
    const calledElement = ref<string | undefined>('')

    const getElementInitiator = () => {
      initiator.value = getSkipExpression(getActive.value!) || ""
    }

    const getElementAsync = () => {
      isAsync.value = getAsync(getActive.value!)
    }
    const setElementAsync = (value: boolean | false) => {
      setAsync(getActive.value!, value)
    }


    const setElementInitiator = (value: string | undefined) => {
      setSkipExpression(getActive.value!, value)
    }

    const setElementCalledElement = (value: string | undefined) => {
      setCalledElement(getActive.value!, value)
    }
    const getElementCalledElemen = () => {
      calledElement.value = getCalledElement(getActive.value!)
    }


    const getElementStringValueByKey = (f,key: string) => {
      let stringValueByKey = getStringValueByKey(getActive.value!, key);
      f.value=stringValueByKey
      return stringValueByKey;
    }


    onMounted(() => {
      getElementInitiator()
      getElementAsync()
      getElementCalledElemen()

     getElementStringValueByKey(statementId,'statementId');
      getElementStringValueByKey(selectType,'selectType');

      EventEmitter.on('element-update', () => {
        getElementInitiator()
        getElementAsync()
        getElementCalledElemen()
          getElementStringValueByKey(statementId,'statementId');
        getElementStringValueByKey(selectType,'selectType');
      })
    })

    return {
      getActive,
      isAsync,
      initiator,
      setElementInitiator,
      setElementAsync,
      calledElement,
      setElementCalledElement,
      statementId,selectType,
      setStringValueByKey
    }
  }
})
</script>
