import {Components} from 'formiojs';

const FilesComponent = Components.components.file;

class CustomFileComponent extends FilesComponent {
  constructor(data, options, ...args) {
    super(data, options, args);
    this.component.storage = 'base64';
  }

  static override schema(...extend) {
    const schema = FilesComponent.schema({
      label: 'Custom File Upload',
      key: 'file',
      type: 'file',
      storage: 'base64',
      multiple: true,

      ...extend
    });
    return schema;
  }
  static get builderInfo() {
    return {
      title: 'Custom File',
      icon: 'file',
      weight: 20,
      schema: this.schema()
    };
  }
}
export default CustomFileComponent;
