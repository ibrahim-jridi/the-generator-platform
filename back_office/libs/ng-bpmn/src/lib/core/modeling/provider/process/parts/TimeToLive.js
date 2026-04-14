import { html } from 'htm/preact';
import { TextFieldEntry, isTextFieldEntryEdited } from '@bpmn-io/properties-panel';
import { useService } from 'bpmn-js-properties-panel';

export default function(element) {
  return [
    {
      id: 'historyTimeToLive',
      element,
      component: TimeToLive,
      isEdited: isTextFieldEntryEdited
    }
  ];
}

function TimeToLive(props) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.historyTimeToLive;
  };

  const setValue = value => {
    return modeling.updateProperties(element, {
      historyTimeToLive: value
    });
  };

  return html`<${TextFieldEntry}
    id=${ id }
    element=${ element }
    label=${ translate('Time To Live') }
    getValue=${ getValue }
    setValue=${ setValue }
    debounce=${ debounce }
    tooltip=${ translate('Time To Live') }
  />`;
}
