import {html} from 'htm/preact';
import {isSelectEntryEdited, SelectEntry} from '@bpmn-io/properties-panel';
import {useService} from 'bpmn-js-properties-panel';
import {is} from 'bpmn-js/lib/util/ModelUtil';

export default function FormsProvider(forms) {
  return {
    getGroups(element) {
      return function (groups) {
        if (is(element, 'bpmn:UserTask') || is(element, 'bpmn:StartEvent')) {
          groups.push(createFormsGroup(element, forms));
        }

        return groups;
      };
    }
  };
}

function createFormsGroup(element, forms) {
  return {
    id: 'forms',
    label: 'Forms',
    entries: [
      {
        id: 'typeForm',
        element,
        component: (props) => TypeForm(props, forms),
        isEdited: isSelectEntryEdited
      }
    ]
  };
}

function TypeForm(props, forms) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.formKey || '';

  const setValue = (value) => modeling.updateProperties(element, { formKey: value });

  const getFormsItem = () => {
    const formOptions = forms.map((form) => ({ label: form.label + '(' + form.version + ')', value: form.id }));
    return formOptions;
  };

  const isFormRequired = is(element, 'bpmn:UserTask') && !getValue();

  return html`
    <div>
      <${SelectEntry}
        id=${id}
        element=${element}
        label=${translate('Form Key')}
        getValue=${getValue}
        setValue=${setValue}
        getOptions=${getFormsItem}
        debounce=${debounce}
      />
      ${isFormRequired ? html` <div class="bpp-error">${translate('Form is required for UserTask')}</div>` : null}
    </div>
  `;
}
