import EmptyXML from '@/utils/EmptyXML'
import { EditorSettings } from 'types/editor/settings'
import modelerStore from '@/store/modeler'

export const createNewDiagram = async function (newXml?: string, settings?: EditorSettings) {
  try {
    const store = modelerStore()
    const timestamp = Date.now()
    const { processId, processName, processEngine } = settings || {}
    const newId: string = processId ? processId : `Process_${timestamp}`
    const newName: string = processName || `业务流程_${timestamp}`
    const xmlString = newXml || EmptyXML(newId, newName, processEngine)
    const modeler = store.getModeler
    const { warnings } = await modeler!.importXML(xmlString)
    if (warnings && warnings.length) {
      warnings.forEach((warn) => console.warn(warn))
    }
  } catch (e) {
    console.error(`[Process Designer Warn]: ${typeof e === 'string' ? e : (e as Error)?.message}`)
  }
}
