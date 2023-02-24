import PaletteProvider from 'bpmn-js/lib/features/palette/PaletteProvider'
import ElementFactory from 'bpmn-js/lib/features/modeling/ElementFactory.js'
import { assign } from 'min-dash'
import { createAction } from '../utils'

class RewritePaletteProvider extends PaletteProvider {
  private readonly _palette: PaletteProvider
  private readonly _create: any
  private readonly _elementFactory: ElementFactory
  private readonly _spaceTool: any
  private readonly _lassoTool: any
  private readonly _handTool: any
  private readonly _globalConnect: any
  private readonly _translate: any
  private readonly _moddle: any
  constructor(palette, create, elementFactory, spaceTool, lassoTool, handTool, globalConnect) {
    super(palette, create, elementFactory, spaceTool, lassoTool, handTool, globalConnect, 2000)
    this._palette = palette
    this._create = create
    this._elementFactory = elementFactory
    this._spaceTool = spaceTool
    this._lassoTool = lassoTool
    this._handTool = handTool
    this._globalConnect = globalConnect
  }
  getPaletteEntries() {
    const actions = {},
      create = this._create,
      elementFactory = this._elementFactory,
      lassoTool = this._lassoTool,
      handTool = this._handTool,
      globalConnect = this._globalConnect

    function createSqlTask(event) {
      const sqlTask = elementFactory.createShape({ type: 'runflow:SqlTask' })

      create.start(event, sqlTask)
    }

    function createRedisTask(event) {
      const sqlTask = elementFactory.createShape({ type: 'runflow:redisTask' })
      create.start(event, sqlTask)
    }



    function createMyBatisTask(event) {
      const sqlTask = elementFactory.createShape({ type: 'runflow:myBatisTask' })
      create.start(event, sqlTask)
    }


    function createExcelTask(event) {
      const sqlTask = elementFactory.createShape({ type: 'runflow:excelTask' })
      create.start(event, sqlTask)
    }


    function createSubprocess(event) {
      const subProcess = elementFactory.createShape({
        type: 'bpmn:SubProcess',
        x: 0,
        y: 0,
        isExpanded: true
      })

      const startEvent = elementFactory.createShape({
        type: 'bpmn:StartEvent',
        x: 40,
        y: 82,
        parent: subProcess
      })

      create.start(event, [subProcess, startEvent], {
        hints: {
          autoSelect: [startEvent]
        }
      })
    }


    function createParticipant(event) {
      create.start(event, elementFactory.createParticipantShape());
    }



    assign(actions, {
      'hand-tool': {
        group: 'tools',
        className: 'bpmn-icon-hand-tool',
        title: '手型工具',
        action: {
          click: function (event) {
            handTool.activateHand(event)
          }
        }
      },
      'lasso-tool': {
        group: 'tools',
        className: 'bpmn-icon-lasso-tool',
        title: '套索工具',
        action: {
          click: function (event) {
            lassoTool.activateSelection(event)
          }
        }
      },
      'global-connect-tool': {
        group: 'tools',
        className: 'bpmn-icon-connection-multi',
        title: '全局连线',
        action: {
          click: function (event) {
            globalConnect.toggle(event)
          }
        }
      },

      "create.participant-expanded": {
        group: "tools",
        className: "bpmn-icon-participant",
        title: "创建池",
        action: {
          dragstart: createParticipant,
          click: createParticipant
        }
      },

      'tool-separator': {
        group: 'tools',
        separator: true
      },
      'create.start-event': createAction(
        elementFactory,
        create,
        'bpmn:StartEvent',
        'events',
        'bpmn-icon-start-event-none',
        '开始事件'
      ),
      'create.end-event': createAction(
        elementFactory,
        create,
        'bpmn:EndEvent',
        'events',
        'bpmn-icon-end-event-none',
        '结束事件'
      ),
      'events-separator': {
        group: 'events',
        separator: true
      },
      'create.exclusive-gateway': createAction(
        elementFactory,
        create,
        'bpmn:ExclusiveGateway',
        'gateway',
        'bpmn-icon-gateway-none',
        '网关'
      ),
      'create.parallel-gateway': createAction(
        elementFactory,
        create,
        'bpmn:ParallelGateway',
        'gateway',
        'bpmn-icon-gateway-parallel',
        '并行网关'
      ),
      'gateway-separator': {
        group: 'gateway',
        separator: true
      },
      'create.user-task': createAction(
        elementFactory,
        create,
        'bpmn:UserTask',
        'activity',
        'bpmn-icon-user-task-rewrite',
        '执行函数'
      ),
      'create.callActivity': createAction(
          elementFactory,
          create,
          'bpmn:CallActivity',
          'activity',
          'bpmn-icon-call-activity',
          '调用活动'
      ),
      // 'create.sql-task': {
      //   group: 'activity',
      //   className: 'miyue-sql-task',
      //   title: '数据库任务',
      //   action: {
      //     click: createSqlTask,
      //     dragstart: createSqlTask
      //   }
      // },

      'create.redis-task': {
        group: 'activity',
        className: 'miyue-redis-task',
        title: 'redis',
        action: {
          click: createRedisTask,
          dragstart: createRedisTask
        }
      },


      'create.mybatis-task': {
        group: 'activity',
        className: 'miyue-mybatis-task',
        title: 'mybatis',
        action: {
          click: createMyBatisTask,
          dragstart: createMyBatisTask
        }
      },

      'create.excel-task': {
        group: 'activity',
        className: 'miyue-excel-task',
        title: 'mybatis',
        action: {
          click: createExcelTask,
          dragstart: createExcelTask
        }
      },

    })

    return actions
  }
}

RewritePaletteProvider.$inject = [
  'palette',
  'create',
  'elementFactory',
  'spaceTool',
  'lassoTool',
  'handTool',
  'globalConnect'
]

export default RewritePaletteProvider
