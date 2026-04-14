import Ids from 'ids';

import { getBusinessObject } from 'bpmn-js/lib/util/ModelUtil';

export function getParametersExtension(element) {
  const businessObject = getBusinessObject(element);
  return getExtension(businessObject, 'camunda:InputOutput');
}

export function getParameters(element) {
  const parameters = getParametersExtension(element);
  return parameters && parameters.get('values');
}
export function getParametersOutput(element) {
  const parameters = getParametersExtension(element);
  return parameters && parameters.get('valuesOutput');
}

export function getExtension(element, type) {
  const extensionElements = element.extensionElements;

  if (!extensionElements || !Array.isArray(extensionElements.values)) {
    return null;
  }

  return extensionElements.values.find(e => {
    return typeof e.$instanceOf === 'function' && e.$instanceOf(type);
  }) || null;
}


export function createElement(elementType, properties, parent, factory) {
  const element = factory.create(elementType, properties);

  if (parent) {
    element.$parent = parent;
  }

  return element;
}

export function createParameters(properties, parent, bpmnFactory) {
  return createElement('camunda:InputOutput', properties, parent, bpmnFactory);
}


export function nextId(prefix) {
  const ids = new Ids([ 32,32,1 ]);

  return ids.nextPrefixed(prefix);
}
