import {
    getBusinessObject
  } from 'bpmn-js/lib/util/ModelUtil';
  
  import {
    createElement,
    createParameters,
    getParametersExtension,
    getParametersOutput,
    nextId
  } from '../util';
  
  import { without } from 'min-dash';
  import OutputProps from './OutputProps';
  
  export default function OutputsProps({ element, injector }) {
  
    const parameters = getParametersOutput(element) || [];
  
    const bpmnFactory = injector.get('bpmnFactory'),
          commandStack = injector.get('commandStack');
  
    const items = parameters.map((parameter, index) => {
      const id = element.id + '-parameter-' + index;
  
      return {
        id,
        label: parameter.get('name') || '',
        entries: OutputProps({
          idPrefix: id,
          element,
          parameter
        }),
        autoFocusEntry: id + '-name',
        remove: removeFactory({ commandStack, element, parameter })
      };
    });
  
    return {
      items,
      add: addFactory({ element, bpmnFactory, commandStack })
    };
  }
  
  function removeFactory({ commandStack, element, parameter }) {
    return function(event) {
      event.stopPropagation();
  
      const extension = getParametersExtension(element);
  
      if (!extension) {
        return;
      }
  
      const parameters = without(extension.valuesOutput, parameter);
  
      commandStack.execute('element.updateModdleProperties', {
        element,
        moddleElement: extension,
        properties: {
          valuesOutput: parameters
        }
      });
    };
  }
  
  function addFactory({ element, bpmnFactory, commandStack }) {
    return function(event) {
      event.stopPropagation();
  
      const commands = [];
  
      const businessObject = getBusinessObject(element);
  
      let extensionElements = businessObject.get('extensionElements');
  
      if (!extensionElements) {
        extensionElements = createElement(
          'bpmn:ExtensionElements',
          { values: [] },
          businessObject,
          bpmnFactory
        );
  
        commands.push({
          cmd: 'element.updateModdleProperties',
          context: {
            element,
            moddleElement: businessObject,
            properties: { extensionElements }
          }
        });
      }
  
      let extension = getParametersExtension(element);
  
      if (!extension) {
        extension = createParameters({
          values: []
        }, extensionElements, bpmnFactory);
  
        commands.push({
          cmd: 'element.updateModdleProperties',
          context: {
            element,
            moddleElement: extensionElements,
            properties: {
              values: [...extensionElements.get('values'), extension]
            }
          }
        });
      }
  
    
      if (!Array.isArray(extension.valuesOutput)) {
        extension.valuesOutput = [];
      }
  
      const newParameter = createElement('camunda:OutputParameter', {
        name: nextId('Output_')
      }, extension, bpmnFactory);
  
      commands.push({
        cmd: 'element.updateModdleProperties',
        context: {
          element,
          moddleElement: extension,
          properties: {
            valuesOutput: [...extension.valuesOutput, newParameter]
          }
        }
      });
  
      commandStack.execute('properties-panel.multi-command-executor', commands);
    };
  }
  