import { TextFieldEntry, SelectEntry } from '@bpmn-io/properties-panel';
import { useService } from 'bpmn-js-properties-panel';
import { html } from 'htm/preact';

export default function FieldInjectionProps(props) {
  const { extension, element, idPrefix } = props;

  const entries = [
    {
      id: idPrefix + '-name',
      component: Name,
      extension,
      idPrefix,
      element
    },
    {
      id: idPrefix + '-type',
      component: Type,
      extension,
      idPrefix,
      element
    },
    {
      id: idPrefix + '-value',
      component: Value,
      extension,
      idPrefix,
      element
    },
  ];

  return entries;
}

function Name(props) {
  const { idPrefix, element, extension } = props;
  const commandStack = useService('commandStack');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const setValue = (value) => {
    commandStack.execute('element.updateModdleProperties', {
      element,
      moddleElement: extension,
      properties: { name: value }
    });
  };

  const getValue = () => extension.name;

  return TextFieldEntry({
    element: extension,
    id: idPrefix + '-name',
    label: translate('Name'),
    getValue,
    setValue,
    debounce
  });
}

function Type(props) {
  const { idPrefix, element, extension } = props;
  const commandStack = useService('commandStack');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const setValue = (value) => {
    commandStack.execute('element.updateModdleProperties', {
      element,
      moddleElement: extension,
      properties: { type: value }
    });
  };

  const getValue = () => extension.type;

  const getOptions = () => {
    return [
      { label: 'String', value: 'string' },
      { label: 'Expression', value: 'expression' },
    ];
  };

  return html`<${SelectEntry}
    id=${idPrefix + '-type'}
    element=${element}
    label=${translate('Type')}
    getValue=${getValue}
    setValue=${setValue}
    getOptions=${getOptions}
    debounce=${debounce}
  />`;
}

function Value(props) {
  const { idPrefix, element, extension } = props;
  const commandStack = useService('commandStack');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const setValue = (value) => {
    commandStack.execute('element.updateModdleProperties', {
      element,
      moddleElement: extension,
      properties: { value: value }
    });
  };

  const getValue = () => extension.value;

  return TextFieldEntry({
    element: extension,
    id: idPrefix + '-value',
    label: translate('Value'),
    getValue,
    setValue,
    debounce
  });
}
