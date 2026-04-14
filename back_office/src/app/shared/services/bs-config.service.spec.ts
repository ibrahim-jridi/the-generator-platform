import { TestBed } from '@angular/core/testing';

import { BsConfigService } from './bs-config.service';

describe('BsConfigService', () => {
  let service: BsConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BsConfigService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
