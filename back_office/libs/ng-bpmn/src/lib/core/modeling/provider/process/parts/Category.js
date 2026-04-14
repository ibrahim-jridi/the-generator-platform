import { isSelectEntryEdited, SelectEntry } from '@bpmn-io/properties-panel';
import { useService } from 'bpmn-js-properties-panel';
import { is } from 'bpmn-js/lib/util/ModelUtil';
import { html } from 'htm/preact';

export default function CategoryProvider(element){
  return [
    {
      id: 'categoryProcess',
      element,
      component: Category,
      isEdited: isSelectEntryEdited
    }
  ];
}
function Category(props, forms) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.categoryProcess || '';

  const setValue = (value) => modeling.updateProperties(element, { categoryProcess: value });

  const getOptions = () => {
    return [
      {label: 'Création d\'entreprise', value: 'creationEntreprise'},
    ];
  }


  return html`

      <${SelectEntry}
        id=${id}
        element=${element}
        label=${translate('Category')}
        getValue=${getValue}
        setValue=${setValue}
        getOptions=${getOptions}
        debounce=${debounce}
      />
  `;
}
