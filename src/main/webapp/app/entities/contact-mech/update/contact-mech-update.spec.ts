import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IContactMechType } from 'app/entities/contact-mech-type/contact-mech-type.model';
import { ContactMechTypeService } from 'app/entities/contact-mech-type/service/contact-mech-type.service';
import { IContactMech } from '../contact-mech.model';
import { ContactMechService } from '../service/contact-mech.service';

import { ContactMechFormService } from './contact-mech-form.service';
import { ContactMechUpdate } from './contact-mech-update';

describe('ContactMech Management Update Component', () => {
  let comp: ContactMechUpdate;
  let fixture: ComponentFixture<ContactMechUpdate>;
  let activatedRoute: ActivatedRoute;
  let contactMechFormService: ContactMechFormService;
  let contactMechService: ContactMechService;
  let contactMechTypeService: ContactMechTypeService;

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

    fixture = TestBed.createComponent(ContactMechUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contactMechFormService = TestBed.inject(ContactMechFormService);
    contactMechService = TestBed.inject(ContactMechService);
    contactMechTypeService = TestBed.inject(ContactMechTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ContactMechType query and add missing value', () => {
      const contactMech: IContactMech = { id: 29939 };
      const contactMechType: IContactMechType = { id: 10228 };
      contactMech.contactMechType = contactMechType;

      const contactMechTypeCollection: IContactMechType[] = [{ id: 10228 }];
      vi.spyOn(contactMechTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: contactMechTypeCollection })));
      const additionalContactMechTypes = [contactMechType];
      const expectedCollection: IContactMechType[] = [...additionalContactMechTypes, ...contactMechTypeCollection];
      vi.spyOn(contactMechTypeService, 'addContactMechTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contactMech });
      comp.ngOnInit();

      expect(contactMechTypeService.query).toHaveBeenCalled();
      expect(contactMechTypeService.addContactMechTypeToCollectionIfMissing).toHaveBeenCalledWith(
        contactMechTypeCollection,
        ...additionalContactMechTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.contactMechTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const contactMech: IContactMech = { id: 29939 };
      const contactMechType: IContactMechType = { id: 10228 };
      contactMech.contactMechType = contactMechType;

      activatedRoute.data = of({ contactMech });
      comp.ngOnInit();

      expect(comp.contactMechTypesSharedCollection()).toContainEqual(contactMechType);
      expect(comp.contactMech).toEqual(contactMech);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IContactMech>();
      const contactMech = { id: 5019 };
      vi.spyOn(contactMechFormService, 'getContactMech').mockReturnValue(contactMech);
      vi.spyOn(contactMechService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactMech });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(contactMech);
      saveSubject.complete();

      // THEN
      expect(contactMechFormService.getContactMech).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contactMechService.update).toHaveBeenCalledWith(expect.objectContaining(contactMech));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IContactMech>();
      const contactMech = { id: 5019 };
      vi.spyOn(contactMechFormService, 'getContactMech').mockReturnValue({ id: null });
      vi.spyOn(contactMechService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactMech: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(contactMech);
      saveSubject.complete();

      // THEN
      expect(contactMechFormService.getContactMech).toHaveBeenCalled();
      expect(contactMechService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IContactMech>();
      const contactMech = { id: 5019 };
      vi.spyOn(contactMechService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactMech });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contactMechService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContactMechType', () => {
      it('should forward to contactMechTypeService', () => {
        const entity = { id: 10228 };
        const entity2 = { id: 22225 };
        vi.spyOn(contactMechTypeService, 'compareContactMechType');
        comp.compareContactMechType(entity, entity2);
        expect(contactMechTypeService.compareContactMechType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
