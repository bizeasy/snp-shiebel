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

describe('ReturnHeader e2e test', () => {
  const returnHeaderPageUrl = '/return-header';
  let username: string;
  let password: string;
  const returnHeaderSample = {};

  let returnHeader;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/return-headers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/return-headers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/return-headers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (returnHeader) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/return-headers/${returnHeader.id}`,
      }).then(() => {
        returnHeader = undefined;
      });
    }
  });

  it('ReturnHeaders menu should load ReturnHeaders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('return-header');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReturnHeader').should('exist');
    cy.location('pathname').should('eq', returnHeaderPageUrl);
  });

  describe('ReturnHeader page', () => {
    it('should have translated page title', () => {
      cy.visit(returnHeaderPageUrl);
      cy.getEntityHeading('ReturnHeader').should('not.contain', 'snpApp.returnHeader.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(returnHeaderPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReturnHeader page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${returnHeaderPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ReturnHeader');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnHeaderPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/return-headers',
          body: returnHeaderSample,
        }).then(({ body }) => {
          returnHeader = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/return-headers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/return-headers?page=0&size=20>; rel="last",<http://localhost/api/return-headers?page=0&size=20>; rel="first"',
              },
              body: [returnHeader],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(returnHeaderPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ReturnHeader page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('returnHeader');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnHeaderPageUrl);
      });

      it('edit button click should load edit ReturnHeader page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReturnHeader');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnHeaderPageUrl);
      });

      it('edit button click should load edit ReturnHeader page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReturnHeader');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnHeaderPageUrl);
      });

      it('last delete button click should delete instance of ReturnHeader', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('returnHeader').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnHeaderPageUrl);

        returnHeader = undefined;
      });
    });
  });

  describe('new ReturnHeader page', () => {
    beforeEach(() => {
      cy.visit(returnHeaderPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ReturnHeader');
    });

    it('should create an instance of ReturnHeader', () => {
      cy.get(`[data-cy="needsInventoryReceive"]`).should('not.be.checked');
      cy.get(`[data-cy="needsInventoryReceive"]`).click();
      cy.get(`[data-cy="needsInventoryReceive"]`).should('be.checked');

      cy.get(`[data-cy="supplierRmaId"]`).type('huff brr newsstand');
      cy.get(`[data-cy="supplierRmaId"]`).should('have.value', 'huff brr newsstand');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        returnHeader = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', returnHeaderPageUrl);
    });
  });
});
