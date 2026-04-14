import { html } from 'htm/preact';
import { TextFieldEntry, isTextFieldEntryEdited } from '@bpmn-io/properties-panel';
import { useService } from 'bpmn-js-properties-panel';

export default function(element) {
  return [
    {
      id: 'initiator',
      element,
      component: Initiator,
      isEdited: isTextFieldEntryEdited
    }
  ];
}

function Initiator(props) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.initiator || '';
  };

  const setValue = value => {
    return modeling.updateProperties(element, {
        initiator: value
    });
  };

  return html`<${TextFieldEntry}
    id=${ id }
    element=${ element }
    label=${ translate('Initiator') }
    getValue=${ getValue }
    setValue=${ setValue }
    debounce=${ debounce }
   
  />`;
}
