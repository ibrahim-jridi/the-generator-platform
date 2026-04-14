import { html } from 'htm/preact';
import { CheckboxEntry, TextFieldEntry, isCheckboxEntryEdited, isTextFieldEntryEdited } from '@bpmn-io/properties-panel';
import { useService } from 'bpmn-js-properties-panel';

export default function(element) {
  return [
    {
      id: 'startable',
      element,
      component: Startable,
      isEdited: isCheckboxEntryEdited
    }
  ];
}

function Startable(props) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.startable || '';
  };

  const setValue = value => {
    return modeling.updateProperties(element, {
        startable: value
    });
  };

  return html`<${CheckboxEntry}
    id=${ id }
    element=${ element }
    label=${ translate('Startable') }
    getValue=${ getValue }
    setValue=${ setValue }
    debounce=${ debounce }
    tooltip=${ translate('Startable') }
  />`;
}
