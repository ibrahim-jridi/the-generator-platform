import { html } from 'htm/preact';
import { CheckboxEntry, isCheckboxEntryEdited } from '@bpmn-io/properties-panel';
import { useService } from 'bpmn-js-properties-panel';

export default function OutMappingPropagationProps(element) {
  const allVariables = element.businessObject.allVariables || false;
  const local = element.businessObject.local || false;

  return [
    {
      id: 'outVariablesCheck',
      element,
      component: outVariablesCheck,
      isEdited: isCheckboxEntryEdited
    },
    {
      id: 'outLocalCheck',
      element,
      component: outLocalCheck,
      isEdited: isCheckboxEntryEdited
    }
  ];
}

function outVariablesCheck(props) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.outVariablesCheck;

  const setValue = (value) => {
    modeling.updateProperties(element, { outVariablesCheck: value });
  };

  return html` <${CheckboxEntry}
    id=${id}
    element=${element}
    label=${translate('Propagate all variables')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}

function outLocalCheck(props) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.outLocalCheck || false;

  const setValue = (value) => {
    modeling.updateProperties(element, { outLocalCheck: value });
  };

  return html` <${CheckboxEntry}
    id=${id}
    element=${element}
    label=${translate('Local')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}
