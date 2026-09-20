import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IKeywordType } from '../keyword-type.model';
import { KeywordTypeService } from '../service/keyword-type.service';

import { KeywordTypeFormService } from './keyword-type-form.service';
import { KeywordTypeUpdate } from './keyword-type-update';

describe('KeywordType Management Update Component', () => {
  let comp: KeywordTypeUpdate;
  let fixture: ComponentFixture<KeywordTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let keywordTypeFormService: KeywordTypeFormService;
  let keywordTypeService: KeywordTypeService;

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

    fixture = TestBed.createComponent(KeywordTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    keywordTypeFormService = TestBed.inject(KeywordTypeFormService);
    keywordTypeService = TestBed.inject(KeywordTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const keywordType: IKeywordType = { id: 31809 };

      activatedRoute.data = of({ keywordType });
      comp.ngOnInit();

      expect(comp.keywordType).toEqual(keywordType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IKeywordType>();
      const keywordType = { id: 27532 };
      vi.spyOn(keywordTypeFormService, 'getKeywordType').mockReturnValue(keywordType);
      vi.spyOn(keywordTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ keywordType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(keywordType);
      saveSubject.complete();

      // THEN
      expect(keywordTypeFormService.getKeywordType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(keywordTypeService.update).toHaveBeenCalledWith(expect.objectContaining(keywordType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IKeywordType>();
      const keywordType = { id: 27532 };
      vi.spyOn(keywordTypeFormService, 'getKeywordType').mockReturnValue({ id: null });
      vi.spyOn(keywordTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ keywordType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(keywordType);
      saveSubject.complete();

      // THEN
      expect(keywordTypeFormService.getKeywordType).toHaveBeenCalled();
      expect(keywordTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IKeywordType>();
      const keywordType = { id: 27532 };
      vi.spyOn(keywordTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ keywordType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(keywordTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
