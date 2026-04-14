import {is} from 'bpmn-js/lib/util/ModelUtil';
import {ListGroup} from '@bpmn-io/properties-panel';
import OutputsProps from './parts/OutputsProps';
import InputsProps from './parts/InputsProps';

const LOW_PRIORITY = 500;

export default function BusinessRuleTaskPropertiesProvider(propertiesPanel, translate, eventBus, injector) {
  this.getGroups = function (element) {
    return function(groups) {



      if (is(element, 'bpmn:BusinessRuleTask') || is(element, 'bpmn:ServiceTask') || is(element, 'bpmn:SendTask') ) {

        const type = element.businessObject.type;
        if(type =="connector"){
          groups.push(createInputsGroup(element, injector, translate));
          groups.push(createOutputsGroup(element, injector, translate));
        }
      }
      return groups;
    };
  }

  propertiesPanel.registerProvider(LOW_PRIORITY, this);
}

BusinessRuleTaskPropertiesProvider.$inject = ['propertiesPanel', 'translate', 'eventBus', 'injector'];

function createInputsGroup(element, injector, translate) {

  const parametersGroup = {
    id: 'inputs',
    label: translate('Connector Inputs'),
    component: ListGroup,
    ...InputsProps({ element, injector })
  };

  return parametersGroup;
}
function createOutputsGroup(element, injector, translate) {

  const parametersGroup = {
    id: 'outputs',
    label: translate('Connector Outputs'),
    component: ListGroup,
    ...OutputsProps({ element, injector })
  };

  return parametersGroup;
}
