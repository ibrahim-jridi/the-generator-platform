import { TextFieldEntry, isTextFieldEntryEdited } from "@bpmn-io/properties-panel";
import { useService } from "dmn-js-properties-panel";
import { html } from 'htm/preact';

export default function(element) {
  return [
    {
      id: 'historyTimeToLive',
      element,
      component: HistoryTimeToLive,
      isEdited: isTextFieldEntryEdited
    }
  ];
}

function HistoryTimeToLive(props) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.historyTimeToLive || '';
  };

  const setValue = value => {
    return modeling.updateProperties(element, {
        historyTimeToLive: value
    });
  };

  return html`<${TextFieldEntry}
    id=${ id }
    element=${ element }
    label=${ translate('Time to live ') }
    getValue=${ getValue }
    setValue=${ setValue }
    debounce=${ debounce }
    tooltip=${ translate('Time to live ') }
  />`
  ;
}
