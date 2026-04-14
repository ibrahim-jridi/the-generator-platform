import CustomTabComponent from "../../app/shared/CustomTabComponent";
import CustomButtonComponent from "../../app/shared/custom-button-component";
import CustomDataGridComponent from "../../app/shared/DataGridComponent";
import CustomFileComponent from "../../app/shared/custom-file-component";
import CustomSaveDraftButtonComponent from "../../app/shared/custom-save-draft-button-component";
import CustomSubmitButtonComponent from "../../app/shared/custom-submit-button-component";

export const options = {
  builder: {
    custom: {
      title: 'Custom Components',
      weight: 10,
      components: {
        CustomDataGrid: CustomDataGridComponent.builderInfo,
        customTab: CustomTabComponent.builderInfo,
        CustomButtonClick: CustomButtonComponent.builderInfo,
        CustomFile: CustomFileComponent.builderInfo,
        CustomSubmit: CustomSubmitButtonComponent.builderInfo,
        SaveDraft: CustomSaveDraftButtonComponent.builderInfo,
      }
    }


  },
  editForm: {
    CustomDataGrid: [
      {
        label: 'Data Grid',
        key: 'customdatagrid',
        type: 'customdatagrid',
        input: false,
        persistent: false
      }
    ],
    customTab: [
      {
        label: 'Tabs',
        key: 'customtab',
        type: 'customtab',
        input: false,
        persistent: false,
        components: []
      }],
    CustomButtonClick: [
      {
        label: 'Custom Button',
        key: 'custombuttonclick',
        type: 'button',
        input: false,
        persistent: false
      }
    ],
    CustomSubmit: [
      {
        label: 'Soumettre',
        key: 'customSubmit',
        type: 'button',
        input: false,
        persistent: false
      }
    ],
    SaveDraft: [
      {
        label: 'Enregistrer comme brouillon',
        key: 'saveDraft',
        type: 'button',
        input: false,
        persistent: false
      }
    ],
    CustomFile: [
      {
        label: 'Custom File Upload',
        key: 'file',
        type: 'file',
        input: false,
        persistent: false
      }
    ]
  }
};
