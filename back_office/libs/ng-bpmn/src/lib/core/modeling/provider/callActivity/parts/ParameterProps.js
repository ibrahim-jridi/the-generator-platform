import {
  CheckboxEntry, SelectEntry, TextAreaEntry,
  TextFieldEntry
} from '@bpmn-io/properties-panel';

import { useService } from 'bpmn-js-properties-panel';
import { html } from 'htm/preact';

export default function ParameterPropsIn(props) {

  const {
    idPrefix,
    parameter
  } = props;

  const entries = [
    {
      id: idPrefix + '-type',
      component: Source,
      idPrefix,
      parameter
    },
    {
      id: idPrefix + '-target',
      component: Target,
      idPrefix,
      parameter
    }
    // {
    //   id: idPrefix + '-local',
    //   component: Local,
    //   idPrefix,
    //   parameter
    // }
  ];

  return entries;
}



function Source(props) {
  const {
    idPrefix,
    element,
    parameter
  } = props;

  const commandStack = useService('commandStack');
  const translate = useService('translate');
  const debounce = useService('debounceInput');
  const getOptionsType = () => {
    return [
      { label: 'Source', value: 'source' },
      { label: 'Source Expression', value: 'sourceExpression' },
    ];
  };
  const getValue = (parameter) => {
    return parameter.type;
  };

  const setValue = (value) => {
    parameter.assignmentType = value;
    commandStack.execute('element.updateModdleProperties', {
      element,
      moddleElement: parameter,
      properties: {
        type: value
      }
    });
  };
  const showTypeSource = parameter.type === 'source';
  const showTypeSourceExpression = parameter.type === 'sourceExpression';

  return html`
    <${SelectEntry}
      id=${idPrefix + '-type'}
      element=${element}
      label=${translate('Type')}
      getValue=${getValue}
      setValue=${setValue}
      getOptions=${getOptionsType}
      debounce=${debounce}
    />
    ${showTypeSource && html`
      <${TextFieldEntry}
        element=${parameter}
        id=${idPrefix + '-source'}
        label=${translate('Source')}
        getValue=${() => parameter.source}
        setValue=${(value) => commandStack.execute('element.updateModdleProperties', {
          element,
          moddleElement: parameter,
          properties: { source: value }
        })}
        debounce=${debounce}
      />
      `}
      ${showTypeSourceExpression && html`
        <${TextFieldEntry}
          element=${parameter}
          id=${idPrefix + '-sourceExpression'}
          label=${translate('Source Expression')}
          getValue=${() => parameter.sourceExpression}
          setValue=${(value) => commandStack.execute('element.updateModdleProperties', {
            element,
            moddleElement: parameter,
            properties: { sourceExpression: value }
          })}
          debounce=${debounce}
        />

    `}

  `;
}
function Target(props) {
  const {
    idPrefix,
    element,
    parameter
  } = props;

  const commandStack = useService('commandStack');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = (parameter) => {
    return parameter.target;
  };

  const setValue = (value) => {
    parameter.assignmentType = value;
    commandStack.execute('element.updateModdleProperties', {
      element,
      moddleElement: parameter,
      properties: {
        target: value
      }
    });
  };

  return html`<${TextFieldEntry}
    id=${idPrefix + '-target'}
    element=${ element }
    label=${ translate('Target') }
    getValue=${ getValue }
    setValue=${ setValue }
    debounce=${ debounce }


  />`;
}
