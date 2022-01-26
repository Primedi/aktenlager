import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RaumDetailComponent } from './raum-detail.component';

describe('Raum Management Detail Component', () => {
  let comp: RaumDetailComponent;
  let fixture: ComponentFixture<RaumDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RaumDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ raum: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RaumDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RaumDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load raum on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.raum).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
