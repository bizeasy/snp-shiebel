import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IQuoteType } from '../quote-type.model';
import { QuoteTypeService } from '../service/quote-type.service';

import { QuoteTypeFormService } from './quote-type-form.service';
import { QuoteTypeUpdate } from './quote-type-update';

describe('QuoteType Management Update Component', () => {
  let comp: QuoteTypeUpdate;
  let fixture: ComponentFixture<QuoteTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let quoteTypeFormService: QuoteTypeFormService;
  let quoteTypeService: QuoteTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(QuoteTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quoteTypeFormService = TestBed.inject(QuoteTypeFormService);
    quoteTypeService = TestBed.inject(QuoteTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const quoteType: IQuoteType = { id: 5628 };

      activatedRoute.data = of({ quoteType });
      comp.ngOnInit();

      expect(comp.quoteType).toEqual(quoteType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IQuoteType>();
      const quoteType = { id: 19838 };
      vi.spyOn(quoteTypeFormService, 'getQuoteType').mockReturnValue(quoteType);
      vi.spyOn(quoteTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(quoteType);
      saveSubject.complete();

      // THEN
      expect(quoteTypeFormService.getQuoteType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quoteTypeService.update).toHaveBeenCalledWith(expect.objectContaining(quoteType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IQuoteType>();
      const quoteType = { id: 19838 };
      vi.spyOn(quoteTypeFormService, 'getQuoteType').mockReturnValue({ id: null });
      vi.spyOn(quoteTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(quoteType);
      saveSubject.complete();

      // THEN
      expect(quoteTypeFormService.getQuoteType).toHaveBeenCalled();
      expect(quoteTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IQuoteType>();
      const quoteType = { id: 19838 };
      vi.spyOn(quoteTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quoteTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
