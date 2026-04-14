import { is } from 'bpmn-js/lib/util/ModelUtil';

import ConditionExpressionProps from './parts/conditionExpressionProps';

const LOW_PRIORITY = 500;

export default function SequenceFlowPropertiesProvider(propertiesPanel, translate, eventBus, injector) {
  this.getGroups = function(element) {
    return function(groups) {

      if (is(element, 'bpmn:SequenceFlow')) {
        // this condition not include in event Type Bpmn
        if (!is(element.source, 'bpmn:Event')) {
          groups.push(createConditionExpressionGroup(element, translate));

          const type = element.businessObject.type;
          if (type === "script" || type === "expression") {
            const conditionExpression = element.businessObject.$model.create('bpmn:FormalExpression', {
              body: ''
            });

            element.businessObject.conditionExpression = conditionExpression;

            conditionExpression.$parent = element.businessObject;

          }
          else {

            if (element.businessObject.conditionExpression) {
              delete element.businessObject.conditionExpression;
            }

          }

        }
    }
      return groups;
    };
  };

  propertiesPanel.registerProvider(LOW_PRIORITY, this);
}

SequenceFlowPropertiesProvider.$inject = ['propertiesPanel', 'translate', 'eventBus', 'injector'];

//Function to create group of conditionExpression
function createConditionExpressionGroup(element, translate) {

    const ConditionGroup = {
      id: 'type',
      label: translate('Condition'),
      entries: ConditionExpressionProps(element),
      tooltip: translate('Condition')
    };

    return ConditionGroup;

}



