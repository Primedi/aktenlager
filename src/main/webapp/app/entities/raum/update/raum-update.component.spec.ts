import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RaumService } from '../service/raum.service';
import { IRaum, Raum } from '../raum.model';

import { RaumUpdateComponent } from './raum-update.component';

describe('Raum Management Update Component', () => {
  let comp: RaumUpdateComponent;
  let fixture: ComponentFixture<RaumUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let raumService: RaumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RaumUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RaumUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RaumUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    raumService = TestBed.inject(RaumService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const raum: IRaum = { id: 456 };

      activatedRoute.data = of({ raum });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(raum));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Raum>>();
      const raum = { id: 123 };
      jest.spyOn(raumService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raum });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: raum }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(raumService.update).toHaveBeenCalledWith(raum);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Raum>>();
      const raum = new Raum();
      jest.spyOn(raumService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raum });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: raum }));
      saveSubject.complete();

      // THEN
      expect(raumService.create).toHaveBeenCalledWith(raum);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Raum>>();
      const raum = { id: 123 };
      jest.spyOn(raumService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raum });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(raumService.update).toHaveBeenCalledWith(raum);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
