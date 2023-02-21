import ContextPadProvider from 'bpmn-js/lib/features/context-pad/ContextPadProvider'
import { Injector } from 'didi'
import EventBus from 'diagram-js/lib/core/EventBus'
import ContextPad from 'diagram-js/lib/features/context-pad/ContextPad'
import Modeling from 'bpmn-js/lib/features/modeling/Modeling.js'
import ElementFactory from 'bpmn-js/lib/features/modeling/ElementFactory'
import Connect from 'diagram-js/lib/features/connect/Connect'
import Create from 'diagram-js/lib/features/create/Create'
import PopupMenu from 'diagram-js/lib/features/popup-menu/PopupMenu'
import Canvas from 'diagram-js/lib/core/Canvas'
import Rules from 'diagram-js/lib/features/rules/Rules'
import { Translate } from 'diagram-js/lib/i18n/translate'
import {Base, Shape} from 'diagram-js/lib/model'

class RewriteContextPadProvider extends ContextPadProvider {








  constructor(
    config: any,
    injector: Injector,
    eventBus: EventBus,
    contextPad: ContextPad,
    modeling: Modeling,
    elementFactory: ElementFactory,
    connect: Connect,
    create: Create,
    popupMenu: PopupMenu,
    canvas: Canvas,
    rules: Rules,
    translate: Translate
  ) {
    super(
      config,
      injector,
      eventBus,
      contextPad,
      modeling,
      elementFactory,
      connect,
      create,
      popupMenu,
      canvas,
      rules,
      translate,
      2000
    )

    this._contextPad = contextPad
    this._modeling = modeling
    this._elementFactory = elementFactory
    this._connect = connect
    this._create = create
    this._popupMenu = popupMenu
    this._canvas = canvas
    this._rules = rules
    this._translate = translate

    this._autoPlace = injector.get('autoPlace', false)


  }








  getContextPadEntries(element: Base) {
    var that = this;

    const actions: Record<string, any> = {}

    const appendUserTask = (event: Event, element: Shape) => {
      const shape = this._elementFactory.createShape({ type: 'bpmn:UserTask' })
      this._create.start(event, shape, {
        source: element
      })
    }

    const append = this._autoPlace
        ? (event: Event, element: Shape) => {
      console.log(element)
          const shape = this._elementFactory.createShape({ type: 'bpmn:UserTask' })
          this._autoPlace.append(element, shape)
        }
        : appendUserTask


   const removeElement=  (  modeling: Modeling, element: Shape) =>{
      this._modeling.removeElements([element]);
      console.log(element)

    }


  const  startConnect=  (event, element)=> {
     this._connect.start(event, element);
    }


    const  appendAction= (type :string, className :string, title :string, model :string) =>{
      function appendStart(event :Event, element :Shape) {
        var shape = that._elementFactory.createShape({ type: type });
        that._create.start(event, shape, {
          source: element
        });
      }

      var append = that._autoPlace
          ? function (event, element) {
            var shape = that._elementFactory.createShape({ type: type });
            that._autoPlace.append(element, shape);
          }
          : appendStart;

      return {
        group: model,
        className: className,
        title: title,
        action: {
          dragstart: appendStart,
          click: append
        }
      };
    }
    

    // 添加创建用户任务按钮
    actions['append.append-user-task'] = {
      group: 'model',
      className: 'bpmn-icon-user-task-rewrite',
      title: '函数调用',
      action: {
        dragstart: appendUserTask,
        click: append
      }
    }





    // 网关
    actions['append.append-gateway-none'] =appendAction("bpmn:ExclusiveGateway","bpmn-icon-gateway-none","网关","gateway")
    // 网关
    actions['append.append-gateway-parallel'] =appendAction("bpmn:ParallelGateway","bpmn-icon-gateway-parallel","并行网关","gateway")

    // 删除按钮
    actions['append.append-trash'] = {
      group: 'connect',
      className: 'bpmn-icon-trash',
      title: '删除',
      action: {
        click: removeElement
      }
    }
    // 添加创建用户任务按钮
    actions['append.append-connection-multi'] = {
      group: 'connect',
      className: 'bpmn-icon-connection-multi',
      title: '链接',
      action: {
        click: startConnect,
        dragstart: startConnect
      }
    }

    actions['model-redis'] =appendAction("runflow:Redis","miyue-redis-task","redis","model")
    actions['model-mybatis'] =appendAction("runflow:MyBatis","miyue-mybatis-task","mybatis","model")
    // 添加一个新分组的自定义按钮
   // actions['enhancement-op'] =appendAction("miyue:SqlTask","miyue-sql-task","mysql1","enhancement")

    actions['append.end-event'] =appendAction("bpmn:EndEvent","bpmn-icon-end-event-none","结束","enhancement")



    return actions
  }
}


export default RewriteContextPadProvider
