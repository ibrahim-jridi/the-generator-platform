import { CustomButtonModule } from './custom-button.module';

describe('CustomButtonModule', () => {
  let customButtonModule: CustomButtonModule;

  beforeEach(() => {
    customButtonModule = new CustomButtonModule();
  });

  it('should create an instance', () => {
    expect(customButtonModule).toBeTruthy();
  });
});

