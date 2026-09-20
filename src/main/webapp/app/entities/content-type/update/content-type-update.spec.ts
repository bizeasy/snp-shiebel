import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IContentType } from '../content-type.model';
import { ContentTypeService } from '../service/content-type.service';

import { ContentTypeFormService } from './content-type-form.service';
import { ContentTypeUpdate } from './content-type-update';

describe('ContentType Management Update Component', () => {
  let comp: ContentTypeUpdate;
  let fixture: ComponentFixture<ContentTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let contentTypeFormService: ContentTypeFormService;
  let contentTypeService: ContentTypeService;

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

    fixture = TestBed.createComponent(ContentTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contentTypeFormService = TestBed.inject(ContentTypeFormService);
    contentTypeService = TestBed.inject(ContentTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const contentType: IContentType = { id: 14829 };

      activatedRoute.data = of({ contentType });
      comp.ngOnInit();

      expect(comp.contentType).toEqual(contentType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IContentType>();
      const contentType = { id: 8896 };
      vi.spyOn(contentTypeFormService, 'getContentType').mockReturnValue(contentType);
      vi.spyOn(contentTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(contentType);
      saveSubject.complete();

      // THEN
      expect(contentTypeFormService.getContentType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contentTypeService.update).toHaveBeenCalledWith(expect.objectContaining(contentType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IContentType>();
      const contentType = { id: 8896 };
      vi.spyOn(contentTypeFormService, 'getContentType').mockReturnValue({ id: null });
      vi.spyOn(contentTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contentType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(contentType);
      saveSubject.complete();

      // THEN
      expect(contentTypeFormService.getContentType).toHaveBeenCalled();
      expect(contentTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IContentType>();
      const contentType = { id: 8896 };
      vi.spyOn(contentTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contentTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
