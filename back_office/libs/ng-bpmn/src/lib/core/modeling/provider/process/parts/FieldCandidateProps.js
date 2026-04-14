import { html } from 'htm/preact';

import { TextFieldEntry, isTextFieldEntryEdited } from '@bpmn-io/properties-panel';
import { useService } from 'bpmn-js-properties-panel';

export default function(element) {

  return [
    {
      id: 'candidateStarterGroups',
      element,
      component: Groupe,
      isEdited: isTextFieldEntryEdited
    },
    {
        id: 'candidateStarterUsers',
        element,
        component: Users,
        isEdited: isTextFieldEntryEdited
      }
  ];
}

function Groupe(props) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.candidateStarterGroups || '';
  };

  const setValue = value => {
    return modeling.updateProperties(element, {
        candidateStarterGroups: value
    });
  };

  return html`<${TextFieldEntry}
    id=${ id }
    element=${ element }
    description=${ translate('Specify more than one group as comma sparated list') }
    label=${ translate('Candidate starter groups ') }
    getValue=${ getValue }
    setValue=${ setValue }
    debounce=${ debounce }
  />`;
}
function Users(props) {
    const { element, id } = props;
  
    const modeling = useService('modeling');
    const translate = useService('translate');
    const debounce = useService('debounceInput');
  
    const getValue = () => {
      return element.businessObject.candidateStarterUsers || '';
    };
  
    const setValue = value => {
      return modeling.updateProperties(element, {
        candidateStarterUsers: value
      });
    };
  
    return html`<${TextFieldEntry}
      id=${ id }
      element=${ element }
      description=${ translate('Specify more than one group as comma sparated list') }
      label=${ translate('Candidate starter users ') }
      getValue=${ getValue }
      setValue=${ setValue }
      debounce=${ debounce }
    />`;
  }
  