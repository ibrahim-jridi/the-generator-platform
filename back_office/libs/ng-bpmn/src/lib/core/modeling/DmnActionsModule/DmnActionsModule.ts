// import { Injector } from '../Injector';
import { EditorActions } from '../EditorActions';
import { DiagramSelection } from '../DiagramSelection';
import { Injector } from '../Injector';
export default class DmnActionsModule {
  static $inject = ['injector'];
  constructor(injector: Injector) {
    const editorActions = injector.get<EditorActions>('editorActions');
    const selection = injector.get<DiagramSelection>('selection');

    if (editorActions) {
      editorActions.register('cut', () => {
        const selected = selection.get();

        if (selected && selected.length > 0) {
          editorActions.trigger('copy');
          editorActions.trigger('removeSelection');
        }
      });
    }
  }
}
