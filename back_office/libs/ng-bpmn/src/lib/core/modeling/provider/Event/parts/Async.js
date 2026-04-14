import { html } from 'htm/preact';
import { CheckboxEntry, isCheckboxEntryEdited } from '@bpmn-io/properties-panel';
import { useService } from 'bpmn-js-properties-panel';

export default function(element) {
  const asyncBefore = element.businessObject.asyncBefore || false;
  const asyncAfter = element.businessObject.asyncAfter || false;

  return [
    {
      id: 'asyncBefore',
      element,
      component: (props) => AsyncBefore({ ...props, asyncBefore, asyncAfter }),
      isEdited: isCheckboxEntryEdited
    },
    {
      id: 'asyncAfter',
      element,
      component: (props) => AsyncAfter({ ...props, asyncBefore, asyncAfter }),
      isEdited: isCheckboxEntryEdited
    },
    ...(asyncBefore || asyncAfter ? [{
      id: 'exclusive',
      element,
      component: Exclusive,
      isEdited: isCheckboxEntryEdited
    }] : [])
  ];
}

function AsyncBefore(props) {
  const { element, id, asyncBefore, asyncAfter } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return asyncBefore;
  };

  const setValue = value => {
    modeling.updateProperties(element, { asyncBefore: value });
    if (!value && !asyncAfter) {
      modeling.updateProperties(element, { exclusive: false });
    }
  };

  return html`<${CheckboxEntry}
    id=${id}
    element=${element}
    label=${translate('Before')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}

function AsyncAfter(props) {
  const { element, id, asyncBefore, asyncAfter } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return asyncAfter;
  };

  const setValue = value => {
    modeling.updateProperties(element, { asyncAfter: value });
    if (!value && !asyncBefore) {
      modeling.updateProperties(element, { exclusive: false });
    }
  };

  return html`<${CheckboxEntry}
    id=${id}
    element=${element}
    label=${translate('After')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}

function Exclusive(props) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.exclusive || false;
  };

  const setValue = value => {
    return modeling.updateProperties(element, {
      exclusive: value
    });
  };

  return html`<${CheckboxEntry}
    id=${id}
    element=${element}
    label=${translate('Exclusive')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}
