import BpmnRenderer from 'bpmn-js/lib/draw/BpmnRenderer'
import EventBus from 'diagram-js/lib/core/EventBus'
import Styles from 'diagram-js/lib/draw/Styles'
import PathMap from 'bpmn-js/lib/draw/PathMap'
import Canvas from 'diagram-js/lib/core/Canvas'
import TextRenderer from 'bpmn-js/lib/draw/TextRenderer'
import renderEventContent from '@/additional-modules/Renderer/EnhancementRenderer/renderEventContent'
import { append as svgAppend, attr as svgAttr, create as svgCreate } from 'tiny-svg'
import { drawCircle } from '@/additional-modules/Renderer/utils'
import {Shape} from "diagram-js/lib/model";
import {getFillColor, getLabelColor, getSemantic, getStrokeColor} from "bpmn-js/lib/draw/BpmnRenderUtil";
import {transform} from "diagram-js/lib/util/SvgTransformUtil";
import { assign, forEach, isObject } from 'min-dash'
import {
  classes as svgClasses,
} from 'tiny-svg'

class EnhancementRenderer extends BpmnRenderer {
  _styles: Styles
  private _textRenderer: TextRenderer;
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

    this._textRenderer = textRenderer

    // // // 重点！！！在这里执行重绘
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
    this.handlers['bpmn:UserTask'] = (parentGfx, element, attr) => {
      const attrs = {
      }
        let rect=  this. drawRect(parentGfx, element.width, element.height, 10, attrs)
        this.renderImage(parentGfx, './icons/fx.png', {})
        this.renderEmbeddedLabel(parentGfx, element, 'center-middle')


      return rect

    }

    // 自定义节点的绘制
    this.handlers['runflow:SqlTask'] = (parentGfx, element, attr) => {
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


      this.handlers['runflow:myBatisTask'] = (parentGfx, element, attr) => {
          const attrs = {
          }
          let rect=  this. drawRect(parentGfx, element.width, element.height, 10, attrs)
          this.renderImage(parentGfx, './icons/mybatis.png', {})
          this.renderEmbeddedLabel(parentGfx, element, 'center-middle')
        return rect;

      }



      this.handlers['runflow:redisTask'] = (parentGfx, element, attr) => {
          const attrs = {
          }
          let rect=  this. drawRect(parentGfx, element.width, element.height, 10, attrs)
          this.renderImage(parentGfx, './icons/redis.png', {})
          this.renderEmbeddedLabel(parentGfx, element, 'center-middle')
          return rect;

      }

      this.handlers['runflow:excelTask'] = (parentGfx, element, attr) => {
          const attrs = {
          }
          let rect=  this. drawRect(parentGfx, element.width, element.height, 10, attrs)
          this.renderImage(parentGfx, './icons/excel.png', {})
          this.renderEmbeddedLabel(parentGfx, element, 'center-middle')
          return rect;

      }

  }
     renderEmbeddedLabel(parentGfx, element, align) {
        const semantic = getSemantic(element)

        return this.renderLabel(parentGfx, semantic.name, {
            box: element,
            align: align,
            padding: 5,
            style: {
                fill: getLabelColor(element, '', '')
            }
        })
    }



     drawRect(parentGfx, width, height, r, offset, attrs?) {
        if (isObject(offset)) {
            attrs = offset
            offset = 0
        }

        offset = offset || 0

        attrs = this._styles.computeStyle(attrs, {
            stroke: 'black',
            strokeWidth: 2,
            fill: 'white'
        })

        const rect = svgCreate('rect')
        svgAttr(rect, {
            x: offset,
            y: offset,
            width: width - offset * 2,
            height: height - offset * 2,
            rx: r,
            ry: r
        })
        svgAttr(rect, attrs)

        svgAppend(parentGfx, rect)

        return rect
    }



  drawShape<E extends Shape>(parentNode: SVGElement, element: E): SVGRectElement {




    const type = element.type // 获取到类型





    let svgRectElement = super.drawShape(parentNode, element);
    if (type=="miyue:SqlTask") { // or customConfig[type]
      console.log(parentNode)
      if(element.businessObject.name){
       this. renderButtomLabel(parentNode,element)
      }
    }



    return svgRectElement;
  }

    renderImage(parentGfx, href, options) {

        const customIcon = svgCreate('image')
        const attrs = this._styles.computeStyle(options, {
            stroke: 'black',
            strokeWidth: 2,
        })
        const offset = 4
        svgAttr(customIcon, {
            href: href,
            x: 2,
            y: 3,
            width: 60 - offset * 2,
            height: 36 - offset * 2,
            rx: offset,
            ry: offset
        })
        svgAttr(customIcon, attrs)
        svgAppend(parentGfx, customIcon)
        return customIcon
    }


   renderButtomLabel(parentGfx, element) {
    const semantic = getSemantic(element)
    const textBox = this.renderLabel(parentGfx, semantic.name, {
      box: {
        height: 30,
        width: element.height
      },
      align: 'center-bottom',
      style: {
        fill: getLabelColor(element, '#000000', '#000000')
      }
    })


    transform(textBox, 0,100, 0)
  }
   renderLabel(parentGfx, label, options) {
    options = assign(
        {
          size: {
            width: 100
          }
        },
        options
    )


    const text = this._textRenderer.createText(label || '', options)

    svgClasses(text).add('djs-label')

    svgAppend(parentGfx, text)

    return text
  }



     drawPath(parentGfx, d, attrs?) {
        attrs =this._styles. computeStyle(attrs, ['no-fill'], {
            strokeWidth: 2,
            stroke: 'black'
        })

        const path = svgCreate('path')
        svgAttr(path, { d: d })
        svgAttr(path, attrs)

        svgAppend(parentGfx, path)

        return path
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
