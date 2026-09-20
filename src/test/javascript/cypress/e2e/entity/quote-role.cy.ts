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

describe('QuoteRole e2e test', () => {
  const quoteRolePageUrl = '/quote-role';
  let username: string;
  let password: string;
  const quoteRoleSample = {};

  let quoteRole;
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
      url: '/api/parties',
      body: {
        isOrganisation: true,
        organisationName: 'density abacus',
        organisationShortName: 'premise',
        salutation: 'tomorrow',
        firstName: 'Floyd',
        middleName: 'both scholarship',
        lastName: 'Metz',
        gender: 'FEMALE',
        birthDate: '2023-12-14',
        primaryPhone: 'courageously beneath',
        primaryEmail: 'aboard scramble lovingly',
        isTemporaryPassword: true,
        logoImageUrl: 'sauerkraut quadruple ha',
        profileImageUrl: 'dally amount oh',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/quote-roles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/quote-roles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/quote-roles/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/quotes', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/quote-items', {
      statusCode: 200,
      body: [],
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
    if (quoteRole) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/quote-roles/${quoteRole.id}`,
      }).then(() => {
        quoteRole = undefined;
      });
    }
  });

  afterEach(() => {
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
  });

  it('QuoteRoles menu should load QuoteRoles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('quote-role');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('QuoteRole').should('exist');
    cy.location('pathname').should('eq', quoteRolePageUrl);
  });

  describe('QuoteRole page', () => {
    it('should have translated page title', () => {
      cy.visit(quoteRolePageUrl);
      cy.getEntityHeading('QuoteRole').should('not.contain', 'snpApp.quoteRole.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(quoteRolePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create QuoteRole page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${quoteRolePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('QuoteRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteRolePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/quote-roles',
          body: {
            ...quoteRoleSample,
            party,
          },
        }).then(({ body }) => {
          quoteRole = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/quote-roles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/quote-roles?page=0&size=20>; rel="last",<http://localhost/api/quote-roles?page=0&size=20>; rel="first"',
              },
              body: [quoteRole],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(quoteRolePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details QuoteRole page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('quoteRole');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteRolePageUrl);
      });

      it('edit button click should load edit QuoteRole page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QuoteRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteRolePageUrl);
      });

      it('edit button click should load edit QuoteRole page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QuoteRole');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteRolePageUrl);
      });

      it('last delete button click should delete instance of QuoteRole', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('quoteRole').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteRolePageUrl);

        quoteRole = undefined;
      });
    });
  });

  describe('new QuoteRole page', () => {
    beforeEach(() => {
      cy.visit(quoteRolePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('QuoteRole');
    });

    it('should create an instance of QuoteRole', () => {
      cy.get(`[data-cy="party"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        quoteRole = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', quoteRolePageUrl);
    });
  });
});
