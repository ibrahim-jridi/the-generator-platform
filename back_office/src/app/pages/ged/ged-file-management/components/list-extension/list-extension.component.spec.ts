import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListExtensionComponent } from './list-extension.component';

describe('ListRoleComponent', () => {
  let component: ListExtensionComponent;
  let fixture: ComponentFixture<ListExtensionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListExtensionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListExtensionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
