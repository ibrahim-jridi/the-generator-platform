import {PagesModule} from '././pages.module';

describe('FormElementsModule', () => {
  let formElementsModule: PagesModule;

  beforeEach(() => {
    formElementsModule = new PagesModule();
  });

  it('should create an instance', () => {
    expect(formElementsModule).toBeTruthy();
  });
});
