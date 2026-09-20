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

describe('CustRequestType e2e test', () => {
  const custRequestTypePageUrl = '/cust-request-type';
  let username: string;
  let password: string;
  const custRequestTypeSample = { name: 'beside' };

  let custRequestType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cust-request-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cust-request-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cust-request-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (custRequestType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cust-request-types/${custRequestType.id}`,
      }).then(() => {
        custRequestType = undefined;
      });
    }
  });

  it('CustRequestTypes menu should load CustRequestTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cust-request-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CustRequestType').should('exist');
    cy.location('pathname').should('eq', custRequestTypePageUrl);
  });

  describe('CustRequestType page', () => {
    it('should have translated page title', () => {
      cy.visit(custRequestTypePageUrl);
      cy.getEntityHeading('CustRequestType').should('not.contain', 'snpApp.custRequestType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(custRequestTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CustRequestType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${custRequestTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CustRequestType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cust-request-types',
          body: custRequestTypeSample,
        }).then(({ body }) => {
          custRequestType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cust-request-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [custRequestType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(custRequestTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CustRequestType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('custRequestType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestTypePageUrl);
      });

      it('edit button click should load edit CustRequestType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustRequestType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestTypePageUrl);
      });

      it('edit button click should load edit CustRequestType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustRequestType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestTypePageUrl);
      });

      it('last delete button click should delete instance of CustRequestType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('custRequestType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestTypePageUrl);

        custRequestType = undefined;
      });
    });
  });

  describe('new CustRequestType page', () => {
    beforeEach(() => {
      cy.visit(custRequestTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CustRequestType');
    });

    it('should create an instance of CustRequestType', () => {
      cy.get(`[data-cy="name"]`).type('nudge eek');
      cy.get(`[data-cy="name"]`).should('have.value', 'nudge eek');

      cy.get(`[data-cy="description"]`).type('amongst finally considering');
      cy.get(`[data-cy="description"]`).should('have.value', 'amongst finally considering');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        custRequestType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', custRequestTypePageUrl);
    });
  });
});
