import { is } from 'bpmn-js/lib/util/ModelUtil';
import InMappingPropagation from './parts/InMappingPropagationProps';
import outMappingPropagation from './parts/OutMappingPropagationProps';
import { ListGroup } from '@bpmn-io/properties-panel';
import InputsProps from './parts/InputsProps';
import OutputsProps from './parts/OutputsProps';
import ParameterProps from './parts/ParameterProps';
import ParameterPropsIn from './parts/ParameterProps';
import CamundaInMappingProps from './parts/CamundaInMappingProps';

const LOW_PRIORITY = 500;

export default function CallActivityPropertiesProvider(propertiesPanel, translate, eventbus, injector) {
  this.getGroups = function (element) {
    return function (groups) {
      if (is(element, 'bpmn:CallActivity')) {
        groups.push(inMappingPropagationGroup(element, translate));
        groups.push(createInputsGroup(element, injector, translate));
        groups.push(outMappingPropagationGroup(element, translate));
        groups.push(createOutputsGroup(element, injector, translate));
        groups.push(createInputsMappingGroup(element, injector, translate));
      }
      return groups;
    };
  };
  propertiesPanel.registerProvider(LOW_PRIORITY, this);
}

CallActivityPropertiesProvider.$inject = ['propertiesPanel', 'translate', 'eventBus', 'injector'];

function inMappingPropagationGroup(element, translate) {
  const inMappingPropagationGroup = {
    id: 'InMappingPropagation',
    label: translate('In mapping propagation'),
    entries: InMappingPropagation(element)
  };
  return inMappingPropagationGroup;
}

function outMappingPropagationGroup(element, translate) {
  const outMappingPropagationGroup = {
    id: 'outMappingPropagationGroup',
    label: translate('Out mapping propagation'),
    entries: outMappingPropagation(element)
  };
  return outMappingPropagationGroup;
}

function createInputsGroup(element, injector, translate) {
  const parametersGroup = {
    id: 'inputs',
    label: translate('inputs'),
    component: ListGroup,
    ...InputsProps({ element, injector })
  };

  return parametersGroup;
}

function createOutputsGroup(element, injector, translate) {
  const parametersGroup = {
    id: 'outputs',
    label: translate('outputs'),
    component: ListGroup,
    ...OutputsProps({ element, injector })
  };

  return parametersGroup;
}
function createInputsMappingGroup(element, injector, translate) {
  const parametersGroup = {
    id: 'inputsIn',
    label: translate('In mappings'),
    component: ListGroup,
    ...CamundaInMappingProps({ element, injector })
  };

  return parametersGroup;
}
