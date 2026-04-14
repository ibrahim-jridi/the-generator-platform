import { html } from 'htm/preact';
import { isSelectEntryEdited, SelectEntry } from '@bpmn-io/properties-panel';
import { useService } from 'bpmn-js-properties-panel';
import { is } from 'bpmn-js/lib/util/ModelUtil';

export default function CallActivityListProvider(processDefinitions) {
  return {
    getGroups(element) {
      return function (groups) {
        if (is(element, 'bpmn:CallActivity')) {
          groups.push(createCallActivityGroup(element, processDefinitions));
        }
        return groups;
      };
    }
  };
}

function createCallActivityGroup(element, processDefinitions) {
  return {
    id: 'callProcess',
    label: 'Called Element',
    entries: [
      {
        id: 'calledElement',
        element,
        component: (props) => CalledElement(props, processDefinitions),
        isEdited: isSelectEntryEdited
      }
    ]
  };
}

function CalledElement(props, processDefinitions) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.calledElement || '';
  };

  const setValue = (value) => {
    modeling.updateProperties(element, { calledElement: value });
  };

  const getOptions = () => {
    const callActivityOptions = processDefinitions.map((processDefinition) => ({
      label: `${processDefinition.name} : ${processDefinition.key} (${processDefinition.version})`,
      value: processDefinition.key
    }));
    return callActivityOptions;
  };

  return html` <${SelectEntry}
    id=${id}
    element=${element}
    label=${translate('Called element')}
    getValue=${getValue}
    setValue=${setValue}
    getOptions=${getOptions}
    debounce=${debounce}
  />`;
}
