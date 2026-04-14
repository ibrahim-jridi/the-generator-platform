import { html } from 'htm/preact';
import { TextFieldEntry, isTextFieldEntryEdited } from '@bpmn-io/properties-panel';
import { useService } from 'bpmn-js-properties-panel';

export default function(element) {
  return [
    {
      id: 'failedJobRetryTimeCycle',
      element,
      component: FailedJobRetryTimeCycle,
      isEdited: isTextFieldEntryEdited
    },
    {
        id: 'jobPriority',
        element,
        component: JobPriority,
        isEdited: isTextFieldEntryEdited
      }
  ];
}

function FailedJobRetryTimeCycle(props) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.failedJobRetryTimeCycle || '';
  };

  const setValue = value => {
    return modeling.updateProperties(element, {
        failedJobRetryTimeCycle: value
    });
  };

  return html`<${TextFieldEntry}
    id=${ id }
    element=${ element }
    label=${ translate('Retry time cycle') }
    getValue=${ getValue }
    setValue=${ setValue }
    debounce=${ debounce }
   
  />`;
}
function JobPriority(props) {
    const { element, id } = props;
  
    const modeling = useService('modeling');
    const translate = useService('translate');
    const debounce = useService('debounceInput');
  
    const getValue = () => {
      return element.businessObject.jobPriority || '';
    };
  
    const setValue = value => {
      return modeling.updateProperties(element, {
          jobPriority: value
      });
    };
  
    return html`<${TextFieldEntry}
      id=${ id }
      element=${ element }
      label=${ translate('Priority') }
      getValue=${ getValue }
      setValue=${ setValue }
      debounce=${ debounce }
     
    />`;
  }