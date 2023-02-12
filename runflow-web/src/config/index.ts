import { EditorSettings } from 'types/editor/settings'
import { defaultLang } from '@/i18n'

export const defaultSettings: EditorSettings = {
  language: defaultLang,
  processId: `Process_${new Date().getTime()}`,
  processName: `业务流程`,
  processEngine: 'activiti',
  paletteMode: 'rewrite',
  penalMode: 'custom',
  contextPadMode: 'rewrite',
  rendererMode: 'enhancement',
  bg: 'grid-image',
  toolbar: true,
  miniMap: true,
  contextmenu: false,
  customContextmenu: false,
  otherModule: true,
  templateChooser: false,
  useLint: false,
  customTheme: {}
}
