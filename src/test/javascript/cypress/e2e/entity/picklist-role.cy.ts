import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('PicklistRole e2e test', () => {
  const picklistRolePageUrl = '/picklist-role';
  let username: string;
  let password: string;
  const picklistRoleSample = {};

  let picklistRole;
  let pickList;
  let party;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/pick-lists',
      body: { description: 'rear huzzah', pickListDate: '2023-12-14T05:59:59.882Z' },
    }).then(({ body }) => {
      pickList = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/parties',
      body: {
        isOrganisation: false,
        organisationName: 'persecute private',
        organisationShortName: 'holster barracks',
        salutation: 'at',
        firstName: 'Therese',
        middleName: 'why',
        lastName: 'Kuvalis-Stroman',
        gender: 'OTHER',
        birthDate: '2023-12-14',
        primaryPhone: 'trusty shocked',
        primaryEmail: 'manner teriyaki while',
        isTemporaryPassword: false,
        logoImageUrl: 'for utterly design',
        profileImageUrl: 'which duffel although',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/picklist-roles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/picklist-roles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/picklist-roles/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/pick-lists', {
      statusCode: 200,
      body: [pickList],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/role-types', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (picklistRole) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/picklist-roles/${picklistRole.id}`,
      }).then(() => {
        picklistRole = undefined;
      });
    }
  });

  afterEach(() => {
    if (pickList) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pick-lists/${pickList.id}`,
      }).then(() => {
        pickList = undefined;
      });
    }
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
  });

  it('PicklistRoles menu should load PicklistRoles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('picklist-role');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PicklistRole').should('exist');
    cy.location('pathname').should('eq', picklistRolePageUrl);
  });

  describe('PicklistRole page', () => {
    it('should have translated page title', () => {
      cy.visit(picklistRolePageUrl);
      cy.getEntityHeading('PicklistRole').should('not.contain', 'snpApp.picklistRole.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(picklistRolePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PicklistRole page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${picklistRolePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PicklistRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', picklistRolePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/picklist-roles',
          body: {
            ...picklistRoleSample,
            pickList,
            party,
          },
        }).then(({ body }) => {
          picklistRole = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/picklist-roles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/picklist-roles?page=0&size=20>; rel="last",<http://localhost/api/picklist-roles?page=0&size=20>; rel="first"',
              },
              body: [picklistRole],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(picklistRolePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PicklistRole page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('picklistRole');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', picklistRolePageUrl);
      });

      it('edit button click should load edit PicklistRole page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PicklistRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', picklistRolePageUrl);
      });

      it('edit button click should load edit PicklistRole page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PicklistRole');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', picklistRolePageUrl);
      });

      it('last delete button click should delete instance of PicklistRole', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('picklistRole').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', picklistRolePageUrl);

        picklistRole = undefined;
      });
    });
  });

  describe('new PicklistRole page', () => {
    beforeEach(() => {
      cy.visit(picklistRolePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PicklistRole');
    });

    it('should create an instance of PicklistRole', () => {
      cy.get(`[data-cy="pickList"]`).select(1);
      cy.get(`[data-cy="party"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        picklistRole = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', picklistRolePageUrl);
    });
  });
});
