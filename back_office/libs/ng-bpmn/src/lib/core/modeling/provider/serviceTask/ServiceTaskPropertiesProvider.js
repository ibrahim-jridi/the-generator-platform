import { is } from 'bpmn-js/lib/util/ModelUtil';


import FieldInjectionsProps from './parts/FieldInjectionsProps';
import { ListGroup } from '@bpmn-io/properties-panel';

const LOW_PRIORITY = 500;

export default function ServiceTaskPropertiesProvider(propertiesPanel, translate, eventBus, injector) {
  this.getGroups = function(element) {
    return function(groups) {
      if (is(element, 'bpmn:ServiceTask')||is(element ,'bpmn:BusinessRuleTask')) {
        groups.push(createFieldInjectionGroup(element, injector, translate));
      }
      const {sendTask, notification} = element.businessObject;
      if ( is(element, 'bpmn:SendTask') && sendTask) {
        groups.push(createFieldInjectionGroup(element, injector, translate));
      }
      return groups;
    };
  };

  propertiesPanel.registerProvider(LOW_PRIORITY, this);
}

ServiceTaskPropertiesProvider.$inject = ['propertiesPanel', 'translate', 'eventBus', 'injector'];

  function createFieldInjectionGroup(element, injector, translate) {

    const parametersGroup = {
      id: 'fields',
      label: translate('Field Injections'),
      component: ListGroup,
      ...FieldInjectionsProps({ element, injector })
    };

    return parametersGroup;
  }
