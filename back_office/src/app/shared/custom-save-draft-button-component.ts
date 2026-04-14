import {Components} from 'formiojs';

const ButtonCustomComponent = Components.components.button;

class CustomSaveDraftButtonComponent extends ButtonCustomComponent {
  activeTabIndex: number = 0;

  constructor(component, options, data) {
    super(component, options, data);
    this.activeTabIndex = 0;

    if (!options || !options.builder) {
      this.injectCustomCSS();
    }
  }

  static get builderInfo() {
    return {
      title: 'Custom Save Draft Button',
      icon: 'folder-o',
      weight: 50,
      schema: CustomSaveDraftButtonComponent.schema()
    };
  }

  public static override schema(...extend) {
    return ButtonCustomComponent.schema({
      label: 'Enregistrer comme brouillon',
      action: 'custom',
      showValidations: false,
      tableView: false,
      key: 'saveDraft',
      type: 'button',
      input: true,
      custom: 'try {\n' +
          '      const formInstance = instance.root;\n' +
          '      if (formInstance) {\n' +
          '        instance.emit(\'saveDraft\', formInstance.data);\n' +
          '      } else {\n' +
          '        formInstance.showErrors();\n' +
          '      }\n' +
          '    } catch (error) {\n' +
          '      console.error(\'Error in save draft button logic:\', error);\n' +
          '    }'
    });
  }

  override attach(element) {
    element.classList.add('custom-button-class');
    const attached = super.attach(element);

    return attached;
  }

  switchTab(index) {
    this.redraw();
  }

  injectCustomCSS() {
    const css = `.custom-button-class { background-color: lightblue; }`;
    const style = document.createElement('style');
    style.type = 'text/css';
    style.appendChild(document.createTextNode(css));
    document.head.appendChild(style);
  }
}

export default CustomSaveDraftButtonComponent;
Components.addComponent('saveDraft', CustomSaveDraftButtonComponent);

