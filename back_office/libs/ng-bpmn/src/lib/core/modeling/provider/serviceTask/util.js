import Ids from 'ids';

import { getBusinessObject } from 'bpmn-js/lib/util/ModelUtil';

export function getParametersExtension(element) {
  const businessObject = getBusinessObject(element);
  return getExtension(businessObject, 'camunda:Fields');
}

export function getParameters(element) {
  const parameters = getParametersExtension(element);
  return parameters && parameters.get('values');
}


export function getExtension(element, type) {
  if (!element || !element.extensionElements) {
    return null;
  }

  const values = element.extensionElements.values;

  if (!Array.isArray(values)) {
    return null;
  }

  return values.find(e => e && e.$instanceOf && e.$instanceOf(type)) || null;
}


export function createElement(elementType, properties, parent, factory) {
  const element = factory.create(elementType, properties);

  if (parent) {
    element.$parent = parent;
  }

  return element;
}

export function createParameters(properties, parent, bpmnFactory) {
  return createElement('camunda:Fields', properties, parent, bpmnFactory);
}


export function nextId(prefix) {
  const ids = new Ids([ 32,32,1 ]);

  return ids.nextPrefixed(prefix);
}
