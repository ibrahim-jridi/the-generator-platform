import { is } from 'bpmn-js/lib/util/ModelUtil';
import { ListGroup } from '@bpmn-io/properties-panel';
import InputsProps from './parts/InputsProps';
import OutputsProps from './parts/OutputsProps';

const LOW_PRIORITY = 500;

export default function UserTaskPropertiesProvider(propertiesPanel, translate, eventBus, injector) {

  this.getGroups = function(element) {
    return function(groups) {
      if (is(element, 'bpmn:UserTask')) {

        groups.push(createInputsGroup(element, injector, translate));
        groups.push(createOutputsGroup(element, injector, translate));
      }
      if (is(element, 'bpmn:ServiceTask') || is(element, 'bpmn:ReceiveTask')  || is(element, 'bpmn:SubProcess') || is(element ,'bpmn:BusinessRuleTask')) {
        groups.push(createInputsGroup(element, injector, translate));
        groups.push(createOutputsGroup(element, injector, translate));
      }
      if (is(element, 'bpmn:EndEvent')) {
        groups.push(createInputsGroup(element, injector, translate));
      }
      const {sendTask, notification} = element.businessObject;
      if ( is(element, 'bpmn:SendTask') && sendTask) {
        groups.push(createInputsGroup(element, injector, translate));
        groups.push(createOutputsGroup(element, injector, translate));
      }
      return groups;
    };
  };

  propertiesPanel.registerProvider(LOW_PRIORITY, this);
}

UserTaskPropertiesProvider.$inject = ['propertiesPanel', 'translate', 'eventBus', 'injector'];


function createInputsGroup(element, injector, translate) {
  const parametersGroup = {
    id: 'inputs',
    label: translate('Inputs'),
    component: ListGroup,
    ...InputsProps({ element, injector })
  };

  return parametersGroup;
}

function createOutputsGroup(element, injector, translate) {
  const parametersGroup = {
    id: 'outputs',
    label: translate('Outputs'),
    component: ListGroup,
    ...OutputsProps({ element, injector })
  };

  return parametersGroup;
}
