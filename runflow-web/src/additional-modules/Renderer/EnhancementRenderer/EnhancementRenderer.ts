import BpmnRenderer from 'bpmn-js/lib/draw/BpmnRenderer'
import EventBus from 'diagram-js/lib/core/EventBus'
import Styles from 'diagram-js/lib/draw/Styles'
import PathMap from 'bpmn-js/lib/draw/PathMap'
import Canvas from 'diagram-js/lib/core/Canvas'
import TextRenderer from 'bpmn-js/lib/draw/TextRenderer'
import renderEventContent from '@/additional-modules/Renderer/EnhancementRenderer/renderEventContent'
import { append as svgAppend, attr as svgAttr, create as svgCreate } from 'tiny-svg'
import { drawCircle } from '@/additional-modules/Renderer/utils'

class EnhancementRenderer extends BpmnRenderer {
  _styles: Styles
  constructor(
    config: any,
    eventBus: EventBus,
    styles: Styles,
    pathMap: PathMap,
    canvas: Canvas,
    textRenderer: TextRenderer
  ) {
    super(config, eventBus, styles, pathMap, canvas, textRenderer, 3000)

    this._styles = styles

    // // 重点！！！在这里执行重绘
    // this.handlers['bpmn:Event'] = (parentGfx, element, attrs) => {
    //   if (!attrs || !attrs['fillOpacity']) {
    //     !attrs && (attrs = {})
    //     attrs['fillOpacity'] = 1
    //     attrs['fill'] = '#1bbc9d'
    //     attrs['strokeWidth'] = 0
    //   }
    //   return drawCircle(this, parentGfx, element.width, element.height, attrs)
    // }
    // this.handlers['bpmn:EndEvent'] = (parentGfx, element) => {
    //   const circle = this.handlers['bpmn:Event'](parentGfx, element, {
    //     fillOpacity: 1,
    //     strokeWidth: 2,
    //     fill: '#e98885',
    //     stroke: '#000000'
    //   })
    //   renderEventContent(this.handlers, element, parentGfx)
    //   return circle
    // }
    // 自定义节点的绘制
    this.handlers['miyue:SqlTask'] = (parentGfx, element, attr) => {
      const customIcon = svgCreate('image')
      svgAttr(customIcon, {
        ...(attr || {}),
        width: element.width,
        height: element.height,
        href: './icons/mysql.png'
      })
      svgAppend(parentGfx, customIcon)
      return customIcon
    }
  }
}

EnhancementRenderer.$inject = [
  'config.bpmnRenderer',
  'eventBus',
  'styles',
  'pathMap',
  'canvas',
  'textRenderer'
]

export default EnhancementRenderer
