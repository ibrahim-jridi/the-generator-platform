import { html } from 'htm/preact';
import { CheckboxEntry, isCheckboxEntryEdited } from '@bpmn-io/properties-panel';
import { useService } from 'bpmn-js-properties-panel';



export default function InMappingPropagationProps(element) {
  const allVariables = element.businessObject.allVariables || false;
  const local = element.businessObject.local || false;

  return [
    {
      id: 'inVariablesCheck',
      element,
      component: inVariablesCheck,
      isEdited: isCheckboxEntryEdited
    },
    {
      id: 'inLocalCheck',
      element,
      component: inLocalCheck,
      isEdited: isCheckboxEntryEdited
    }
  ];
}

function inVariablesCheck(props) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.inVariablesCheck || false;

  const setValue = (isCheckboxEntryEdited) => {
    modeling.updateProperties(element, { inVariablesCheck: isCheckboxEntryEdited });
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

function inLocalCheck(props) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.inLocalCheck || false;

  const setValue = (value) => {
    modeling.updateProperties(element, { inLocalCheck: value });
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
